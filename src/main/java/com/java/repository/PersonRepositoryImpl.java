package com.java.repository;

import com.java.controller.SecurityController;
import com.java.model.Person;
import com.java.model.PersonStatus;
import com.java.util.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonRepositoryImpl implements PersonRepository {

    @Override
    public Person entrance(Person person) {
        SecurityController securityController = new SecurityController();
        Person authorizedPerson = null;
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Person WHERE login = ? AND hashPassword = ?");
            statement.setString(1, person.getLogin());
            statement.setString(2, person.getPassword());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String login = resultSet.getString("login");
                String password = resultSet.getString("hashPassword");
                if (person.getLogin().equals(login) && person.getPassword().equals(password)) {
                    String username = resultSet.getString("username");
                    Long id = resultSet.getLong("id");
                    String status = resultSet.getString("status");
                    Integer cash = resultSet.getInt("cash");
                    authorizedPerson = new Person(id, username, login, password, cash, status);
                    System.out.println("\nВы успешно вошли в систему!\n");
                }
            }
            if (authorizedPerson == null) {
                System.err.println("\nОшибка входа в систему!");
                securityController.start();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка в методе entrance");
        }
        return authorizedPerson;
    }

    @Override
    public Person create(Person person) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Person (username, login, hashPassword, cash, status) VALUES (?,?,?,?,?)");
            statement.setString(1, person.getUsername());
            statement.setString(2, person.getLogin());
            statement.setString(3, person.getPassword());
            statement.setInt(4, person.getCash());
            statement.setString(5, person.getStatus());
            statement.execute();
            PreparedStatement getterIdStatement = connection.prepareStatement("SELECT id FROM Person WHERE username = ?");
            getterIdStatement.setString(1, person.getUsername());
            ResultSet idResult = getterIdStatement.executeQuery();
            while (idResult.next()) {
                long id = idResult.getLong("id");
                if (id == 1) {
                    PreparedStatement insertStatement = connection.prepareStatement("UPDATE Person SET status = ? WHERE id = ?");
                    insertStatement.setString(1, String.valueOf(PersonStatus.ADMIN));
                    insertStatement.setLong(2, id);
                    insertStatement.execute();
                    person.setStatus(String.valueOf(PersonStatus.ADMIN));
                }
                if (id == 2) {
                    PreparedStatement insertStatement = connection.prepareStatement("UPDATE Person SET status = ? WHERE id = ?");
                    insertStatement.setString(1, String.valueOf(PersonStatus.MANAGER));
                    insertStatement.setLong(2, id);
                    insertStatement.execute();
                    person.setStatus(String.valueOf(PersonStatus.MANAGER));
                }
                person.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("\nАккаунт успешно создан!\n");
        return person;
    }

    @Override
    public Person read(Long id) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Person WHERE id = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            Person person = null;
            while (resultSet.next()) {
                Long currentId = resultSet.getLong("id");
                String username = resultSet.getString("username");
                String login = resultSet.getString("login");
                String password = resultSet.getString("hashPassword");
                String status = resultSet.getString("status");
                Integer cash = resultSet.getInt("cash");
                person = new Person(currentId, username, login, password, cash, status);
            }
            return person;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка в методе read");
        }
    }

    @Override
    public Person update(Person newT) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement insertStatement = connection.prepareStatement("UPDATE Person SET username = ?, login = ?, hashPassword = ? WHERE id = ?");
            insertStatement.setString(1, newT.getUsername());
            insertStatement.setString(2, newT.getLogin());
            insertStatement.setString(3, newT.getPassword());
            insertStatement.setLong(4, newT.getId());
            insertStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("\nДанные успешно изменены!");
        return newT;
    }

    @Override
    public Person update(Long id, Person person) {
        try (Connection connection = ConnectionManager.open()) {
            if (!(person.getLogin() == null)) {
                PreparedStatement insertStatement = connection.prepareStatement("UPDATE Person SET login = ? WHERE id = ?");
                insertStatement.setString(1, person.getLogin());
                insertStatement.setLong(2, id);
                insertStatement.execute();
            } else if (!(person.getCash() == 0)) {
                PreparedStatement insertStatement = connection.prepareStatement("UPDATE Person SET cash = ? WHERE id = ?");
                insertStatement.setInt(1, person.getCash());
                insertStatement.setLong(2, id);
                insertStatement.execute();
            } else if (!(person.getUsername() == null)) {
                PreparedStatement insertStatement = connection.prepareStatement("UPDATE Person SET username = ? WHERE id = ?");
                insertStatement.setString(1, person.getUsername());
                insertStatement.setLong(2, id);
                insertStatement.execute();
            } else if (!(person.getPassword() == null)) {
                PreparedStatement insertStatement = connection.prepareStatement("UPDATE Person SET hashPassword = ? WHERE id = ?");
                insertStatement.setString(1, person.getPassword());
                insertStatement.setLong(2, id);
                insertStatement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка в методе update");
        }
        System.out.println("\nДанные успешно изменены!");
        return person;
    }

    @Override
    public boolean delete(Long id) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Person WHERE id = ?");
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("\nПользователь успешно удален!");
        return true;
    }

    @Override
    public boolean delete(Person person) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement checkStatement = connection.prepareStatement("SELECT status FROM Person WHERE id = ?");
            checkStatement.setLong(1, person.getId());
            ResultSet resultSet = checkStatement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString("status").equals("ADMIN") || resultSet.getString("status").equals("MANAGER")) {
                    System.err.println("\nНевозможно удалить системный аккаунт!");
                    return false;
                }
            }
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Person WHERE id = ?");
            statement.setLong(1, person.getId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        System.out.println("\nАккаунт успешно удален!");
        return true;
    }

    @Override
    public List<Person> getAllPersons() {
        List<Person> persons = new ArrayList<>();
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Person");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String username = resultSet.getString("username");
                String login = resultSet.getString("login");
                String password = resultSet.getString("hashPassword");
                String status = resultSet.getString("status");
                Integer cash = resultSet.getInt("cash");
                Person person = new Person(id, username, login, password, cash, status);
                persons.add(person);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка в методе getAllPersons");
        }
        return persons;
    }

    @Override
    public boolean idCheck(Long id) {
        boolean isIdExist = false;
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement("SELECT id FROM Person");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long currentId = resultSet.getLong("id");
                if (id.equals(currentId)) {
                    isIdExist = true;
                }
            }
            if (!isIdExist) {
                System.err.println("\nПользователь с таким id не найден!");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка в методе idCheck");
        }
        return isIdExist;
    }

    @Override
    public boolean checkLoginValid(String login) {
        boolean isValid = true;
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement getterUsernameStatement = connection.prepareStatement("SELECT login FROM Person");
            ResultSet nameResult = getterUsernameStatement.executeQuery();
            while (nameResult.next()) {
                if (nameResult.getString("login").equals(login)) {
                    System.err.println("\nВыбранный вами логин уже используется!\n");
                    isValid = false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка в методе checkLoginValid");
        }
        return isValid;
    }

    @Override
    public boolean checkUsernameValid(String username) {
        boolean isValid = true;
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement getterUsernameStatement = connection.prepareStatement("SELECT username FROM Person");
            ResultSet nameResult = getterUsernameStatement.executeQuery();
            while (nameResult.next()) {
                if (nameResult.getString("username").equals(username)) {
                    System.err.println("\nВыбранное вами имя пользователя уже используется!\n");
                    isValid = false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка в методе checkUsernameValid");
        }
        return isValid;
    }

    public void updateCash(Person person) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement cashStatement = connection.prepareStatement("SELECT cash FROM Person WHERE id = ?");
            cashStatement.setLong(1, person.getId());
            ResultSet resultSet = cashStatement.executeQuery();
            while (resultSet.next()) {
                person.addCash(resultSet.getInt("cash"));
            }
            PreparedStatement insertStatement = connection.prepareStatement("UPDATE Person SET cash = ? WHERE id = ?");
            insertStatement.setInt(1, person.getCash());
            insertStatement.setLong(2, person.getId());
            insertStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка в методе updateCash");
        }
        System.out.println("\nДенежные средства добавлены!");
    }

    @Override
    public void updateCashByTicketPrice(Person person) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE Person SET cash = ? WHERE id = ?");
            statement.setInt(1, person.getCash());
            statement.setLong(2, person.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка в методе updateCashByTicketPrice");
        }
    }
}
