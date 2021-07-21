package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);
    private static final ResultSetExtractor<List<User>> SET_EXTRACTOR = getExtractor();

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        ValidationUtil.validate(user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) != 0) {
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
        } else {
            return null;
        }
        saveRoles(List.copyOf(user.getRoles()), user.getId());
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users u  LEFT JOIN user_roles r ON u.id = r.user_id WHERE id=?", SET_EXTRACTOR, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT DISTINCT * FROM users u  LEFT JOIN user_roles r ON u.id = r.user_id  WHERE email=?", SET_EXTRACTOR, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles r ON u.id=r.user_id", SET_EXTRACTOR);
    }

    private void saveRoles(List<Role> roles, int userId) {
        if (roles.isEmpty()) return;
        jdbcTemplate.batchUpdate(
                "INSERT INTO user_roles (role, user_id) VALUES (?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, roles.get(i).name());
                        ps.setInt(2, userId);
                    }

                    public int getBatchSize() {
                        return roles.size();
                    }
                });
    }

    private static ResultSetExtractor<List<User>> getExtractor() {
        return new ResultSetExtractor<List<User>>() {
            @Override
            public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
                Map<Integer, User> map = new HashMap<>();
                int rowNum = 0;
                while (rs.next()) {
                    int id = rs.getInt("id");
                    User user = map.get(id);
                    if (user == null) {
                        user = ROW_MAPPER.mapRow(rs, rowNum++);
                        user.setRoles(Collections.EMPTY_SET);
                        map.put(id, user);
                    }
                    String role = rs.getString("role");
                    if (StringUtils.hasLength(role)) {
                        Role roles = Role.valueOf(rs.getString("role"));
                        user.getRoles().add(roles);
                    }
                }
                return map.values()
                        .stream()
                        .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                        .collect(Collectors.toList());

            }
        };
    }
}
