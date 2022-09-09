package ru.piskunov.web.dao;

import org.springframework.stereotype.Service;
import ru.piskunov.web.exception.CustomException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

@Service
public class ReportCategoryDao {
    private final DataSource dataSource;

    public ReportCategoryDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Optional<ReportCategoryModel> createReportPlus(Long userId, Long categoryId, Date startDate, Date endDate) {
        ReportCategoryModel report;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps;
            ps = connection.prepareStatement("select ct.category_name, " +
                    "                sum(t.amount) as sum " +
                    "from category_transaction as ct " +
                    "         join category_to_transaction as ctt on ct.id = ctt.category_transaction_id " +
                    "         join transaction as t on t.id = ctt.transaction_id " +
                    "         join account as a on a.id = t.to_account_id " +
                    "where a.user_id = ? and ct.id = ? and cast(t.date_and_time as date) between ? and ?" +
                    "group by ct.category_name ");
            report = getReportCategoryModel(userId, categoryId, startDate, endDate, ps);
        } catch (SQLException e) {
            throw new CustomException(e);
        }
        return Optional.ofNullable(report);
    }

    public Optional<ReportCategoryModel> createReportMinus(Long userId, Long categoryId, Date startDate, Date endDate) {
        ReportCategoryModel report;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps;
            ps = connection.prepareStatement("select ct.category_name, " +
                    "                sum(t.amount) as sum " +
                    "from category_transaction as ct " +
                    "         join category_to_transaction as ctt on ct.id = ctt.category_transaction_id " +
                    "         join transaction as t on t.id = ctt.transaction_id " +
                    "         join account as a on a.id = t.from_account_id " +
                    "where a.user_id = ? and ct.id = ? and cast(t.date_and_time as date) between ? and ?" +
                    "group by ct.category_name ");
            report = getReportCategoryModel(userId, categoryId, startDate, endDate, ps);
        } catch (SQLException e) {
            throw new CustomException(e);
        }
        return Optional.ofNullable(report);
    }

    private ReportCategoryModel getReportCategoryModel(Long userId, Long categoryId, Date startDate, Date endDate, PreparedStatement ps) throws SQLException {
        ReportCategoryModel report = null;
        ps.setLong(1, userId);
        ps.setLong(2, categoryId);
        ps.setDate(3, startDate);
        ps.setDate(4, endDate);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            report = new ReportCategoryModel();
            report.setName(rs.getString("category_name"));
            report.setAmount(rs.getLong("sum"));
        }
        return report;
    }
}
