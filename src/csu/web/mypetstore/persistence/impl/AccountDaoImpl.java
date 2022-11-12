package csu.web.mypetstore.persistence.impl;

import csu.web.mypetstore.domain.Account;
import csu.web.mypetstore.persistence.AccountDao;
import csu.web.mypetstore.persistence.DBUtil;
import csu.web.mypetstore.web.servlet.RemoveCartServlet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccountDaoImpl implements AccountDao {
    private static String getAccountByUsernameString = "SELECT SIGNON.USERNAME,ACCOUNT.EMAIL,ACCOUNT.FIRSTNAME," +
            "ACCOUNT.LASTNAME,ACCOUNT.STATUS,ACCOUNT.ADDR1 AS address1,ACCOUNT.ADDR2 AS address2,ACCOUNT.CITY," +
            "ACCOUNT.STATE,ACCOUNT.ZIP,ACCOUNT.COUNTRY,ACCOUNT.PHONE,PROFILE.LANGPREF AS languagePreference," +
            "PROFILE.FAVCATEGORY AS favouriteCategoryId,PROFILE.MYLISTOPT AS listOption,PROFILE.BANNEROPT AS bannerOption," +
            "BANNERDATA.BANNERNAME " +
            "FROM ACCOUNT, PROFILE, SIGNON, BANNERDATA " +
            "WHERE ACCOUNT.USERID = ? AND SIGNON.USERNAME = ACCOUNT.USERID AND PROFILE.USERID = ACCOUNT.USERID " +
            "AND PROFILE.FAVCATEGORY = BANNERDATA.FAVCATEGORY";

    private static String getAccountByUsernameAndPasswordString = "SELECT SIGNON.USERNAME,ACCOUNT.EMAIL,ACCOUNT.FIRSTNAME," +
            "ACCOUNT.LASTNAME,ACCOUNT.STATUS,ACCOUNT.ADDR1 AS address1,ACCOUNT.ADDR2 AS address2,ACCOUNT.CITY," +
            "ACCOUNT.STATE,ACCOUNT.ZIP,ACCOUNT.COUNTRY,ACCOUNT.PHONE,PROFILE.LANGPREF AS languagePreference," +
            "PROFILE.FAVCATEGORY AS favouriteCategoryId,PROFILE.MYLISTOPT AS listOption,PROFILE.BANNEROPT AS bannerOption," +
            "BANNERDATA.BANNERNAME " +
            "FROM ACCOUNT, PROFILE, SIGNON, BANNERDATA " +
            "WHERE ACCOUNT.USERID = ? AND SIGNON.PASSWORD = ?" +
            "AND SIGNON.USERNAME = ACCOUNT.USERID AND PROFILE.USERID = ACCOUNT.USERID " +
            "AND PROFILE.FAVCATEGORY = BANNERDATA.FAVCATEGORY";

    private static String updateAccountString = "UPDATE ACCOUNT SET EMAIL = ?, FIRSTNAME = ?, LASTNAME = ?,STATUS = ?, ADDR1 = ?," +
            "ADDR2 = ?, CITY = ?, STATE = ?,ZIP = ?, COUNTRY = ?, PHONE = ? " +
            "WHERE USERID = ?";

    private static String updateProfileString = "UPDATE PROFILE SET LANGPREF = ?, FAVCATEGORY = ?, MYLISTOPT = ?,BANNEROPT = ? WHERE USERID = ?";

    private static String updateSignonString = "UPDATE SIGNON SET PASSWORD = ? WHERE USERNAME = ?";


    private static String insertAccountString = "INSERT INTO ACCOUNT (EMAIL, FIRSTNAME, LASTNAME, STATUS, ADDR1, ADDR2,CITY, STATE, ZIP, COUNTRY, PHONE, USERID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static String insertProfileString = "INSERT INTO PROFILE (LANGPREF, FAVCATEGORY, MYLISTOPT, BANNEROPT, USERID) VALUES (?, ?, ?, ?, ?)";

    private static String insertSignonString = "INSERT INTO SIGNON (PASSWORD,USERNAME) VALUES (?, ?)";

    @Override
    public Account getAccountByUsername(String username) {
        return null;
    }

    @Override
    public Account getAccountByUsernameAndPassword(Account account) {
        Account accountResult = null;
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(getAccountByUsernameAndPasswordString);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                accountResult = this.resultSetToAccount(resultSet);
            }
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(preparedStatement);
            DBUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }
        return accountResult;
    }

    private Account resultSetToAccount(ResultSet resultSet) throws Exception{
        Account account = new Account();
        account.setUsername(resultSet.getString("username"));
//        account.setPassword(resultSet.getString("password"));
        account.setEmail(resultSet.getString("email"));
        account.setFirstName(resultSet.getString("firstName"));
        account.setLastName(resultSet.getString("lastName"));
        account.setStatus(resultSet.getString("status"));
        account.setAddress1(resultSet.getString("address1"));
        account.setAddress2(resultSet.getString("address2"));
        account.setCity(resultSet.getString("city"));
        account.setState(resultSet.getString("state"));
        account.setZip(resultSet.getString("zip"));
        account.setCountry(resultSet.getString("country"));
        account.setPhone(resultSet.getString("phone"));
        account.setFavouriteCategoryId(resultSet.getString("favouriteCategoryId"));
        account.setLanguagePreference(resultSet.getString("languagePreference"));
        account.setListOption(resultSet.getInt("listOption") == 1);
        account.setBannerOption(resultSet.getInt("bannerOption") == 1);
        account.setBannerName(resultSet.getString("bannerName"));
        return account;
    }

    @Override
//    public boolean insertAccount(Account account) {
//        try {
//            Connection connection=DBUtil.getConnection();
//            PreparedStatement preparedStatement=connection.prepareStatement(INSERT_ACCOUNT);
//            preparedStatement.setString(1,account.getUsername());
//            preparedStatement.setString(2,account.getEmail());
//            preparedStatement.setString(3,account.getFirstName());
//            preparedStatement.setString(4,account.getLanguagePreference());
//            preparedStatement.setString(5,account.getStatus());
//            preparedStatement.setString(6,account.getAddress1());
//            preparedStatement.setString(7,account.getAddress2());
//            preparedStatement.setString(8,account.getCity());
//            preparedStatement.setString(9,account.getState());
//            preparedStatement.setString(10, account.getZip());
//            preparedStatement.setString(11,account.getCountry());
//            preparedStatement.setString(12,account.getPhone());
//            ResultSet resultSet=preparedStatement.executeQuery();
//            if(resultSet.next()){
//                //插入成功
//                System.out.println("插入成功");
//            }else {
//                System.out.println("失败");
//            }
//            DBUtil.closeResultSet(resultSet);
//            DBUtil.closePreparedStatement(preparedStatement);
//            DBUtil.closeConnection(connection);
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }
    public boolean insertAccount(Account account) {
        boolean result = false;
        Connection connection = null;
        PreparedStatement preparedStatement;
        try {
            connection = DBUtil.getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(insertAccountString);
            preparedStatement.setString(1,account.getEmail());
            preparedStatement.setString(2,account.getFirstName());
            preparedStatement.setString(3,account.getLastName());
            preparedStatement.setString(4,account.getStatus());
            preparedStatement.setString(5,account.getAddress1());
            preparedStatement.setString(6,account.getAddress2());
            preparedStatement.setString(7,account.getCity());
            preparedStatement.setString(8,account.getState());
            preparedStatement.setString(9,account.getZip());
            preparedStatement.setString(10,account.getCountry());
            preparedStatement.setString(11,account.getPhone());
            preparedStatement.setString(12,account.getUsername());
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(insertProfileString);
            preparedStatement.setString(1, account.getLanguagePreference());
            preparedStatement.setString(2, account.getFavouriteCategoryId());
            preparedStatement.setInt(3,account.isListOption()?1:0);
            preparedStatement.setInt(4,account.isBannerOption()?1:0);
            preparedStatement.setString(5,account.getUsername());
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(insertSignonString);
            preparedStatement.setString(1, account.getPassword());
            preparedStatement.setString(2,account.getUsername());
            preparedStatement.executeUpdate();


            connection.commit();
            result = true;
            DBUtil.closePreparedStatement(preparedStatement);
            DBUtil.closeConnection(connection);
        }catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean updateAccount(Account account) {
        boolean result = false;
        Connection connection = null;
        PreparedStatement preparedStatement_1;
        PreparedStatement preparedStatement_2;
        PreparedStatement preparedStatement_3;
        try {
            connection = DBUtil.getConnection();
            connection.setAutoCommit(false);
            preparedStatement_1 = connection.prepareStatement(updateAccountString);
            preparedStatement_1.setString(1,account.getEmail());
            preparedStatement_1.setString(2,account.getFirstName());
            preparedStatement_1.setString(3,account.getLastName());
            preparedStatement_1.setString(4,account.getStatus());
            preparedStatement_1.setString(5,account.getAddress1());
            preparedStatement_1.setString(6,account.getAddress2());
            preparedStatement_1.setString(7,account.getCity());
            preparedStatement_1.setString(8,account.getState());
            preparedStatement_1.setString(9,account.getZip());
            preparedStatement_1.setString(10,account.getCountry());
            preparedStatement_1.setString(11,account.getPhone());
            preparedStatement_1.setString(12,account.getUsername());
            preparedStatement_1.executeUpdate();

            preparedStatement_2 = connection.prepareStatement(updateProfileString);
            preparedStatement_2.setString(1, account.getLanguagePreference());
            preparedStatement_2.setString(2, account.getFavouriteCategoryId());
            preparedStatement_2.setInt(3,account.isListOption()?1:0);
            preparedStatement_2.setInt(4,account.isBannerOption()?1:0);
            preparedStatement_2.setString(5,account.getUsername());
            preparedStatement_2.executeUpdate();

            preparedStatement_3 = connection.prepareStatement(updateSignonString);
            preparedStatement_3.setString(1, account.getPassword());
            preparedStatement_3.setString(2,account.getUsername());
            preparedStatement_3.executeUpdate();


            result = true;
            connection.commit();
            DBUtil.closePreparedStatement(preparedStatement_1);
            DBUtil.closePreparedStatement(preparedStatement_2);
            DBUtil.closePreparedStatement(preparedStatement_3);
            DBUtil.closeConnection(connection);
        }catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public boolean findAccountByUsername(String username) {

        boolean result = false;
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(getAccountByUsernameString);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                result=true;
            }
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(preparedStatement);
            DBUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
