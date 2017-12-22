package kz.greetgo.sandbox.db.dao;

import kz.greetgo.sandbox.controller.model.CharmRecord;
import kz.greetgo.sandbox.controller.model.ClientDetails;
import kz.greetgo.sandbox.controller.model.ClientRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ClientDao {


  @Select("select * from Charm where actual = 1 order by name")
  List<CharmRecord> loadCharmList();

  @Select("select c.charm_id charmId," +
    " c.birth_date dateOfBirth," +
    " c.current_gender gender," +
    " c.* from Client c" +
    " where id = #{id} and" +
    " c.actual = 1")
  ClientDetails loadDetails(@Param("id") String id);

  @Update("update client set actual = 0 where id = #{id}")
  void deleteClient(@Param("id") String id);

  @Update("update client set ${fieldName} = #{value} where id = #{clientId}")
  void updateClientField(@Param("clientId") String id, @Param("fieldName") String fieldName,
                         @Param("value") Object value);


  @Insert("insert into client (id, name, surname, patronymic, birth_date, current_gender, charm_id, actual) " +
    "values (#{id}, #{name}, #{surname}, #{patronymic}, #{birth_date}, #{gender}, #{charm_id}, 1); " +
    "insert into client_addr(client, type) values(#{id}, 'fact');" +
    "insert into client_addr(client, type) values(#{id}, 'reg');")
  void insertClient(@Param("id") String id,
                    @Param("name") String name,
                    @Param("surname") String surname,
                    @Param("patronymic") String patronymic,
                    @Param("birth_date") java.sql.Date birthDate,
                    @Param("gender") String gender,
                    @Param("charm_id") String charmId);

  @Select("select c.surname || ' ' || c.name || ' ' || c.patronymic AS fio, " +
    "ch.name AS charm " +
    "from client c join charm ch on (c.charm_id = ch.id) where c.actual = 1 and ch.actual = 1")
  List<ClientRecord> getListOfClients();

  @Select("select c.id as id, " +
    " c.surname || ' ' || c.name || ' ' || c.patronymic AS fio, " +
    "ch.name AS charm, " +
    "extract(year from age(c.birth_date)) as age " +
    "from client c join charm ch on (c.charm_id = ch.id) " +
    "where c.actual = 1 and ch.actual = 1 and " +
    "c.name || c.surname || c.patronymic like '%' || #{filter} || '%'" +
    "order by ${sort} limit #{count} offset #{skipFirst}")
  List<ClientRecord> getLimitedListOfClients(
    @Param("count") int count,
    @Param("skipFirst") int skipFirst,
    @Param("sort") String sort,
    @Param("filter") String filter
  );

  @Select("select count(*) from client where actual = 1")
  long getSizeOfList();

  @Select("select count(id) from client where actual = 1 and " +
    " name || surname || patronymic like '% || #{filter} || %' ")
  long getSizeOfFilteredList(@Param("filter") String filterByFio);

  @Select("select c.id as id, " +
    " c.surname || ' ' || c.name || ' ' || c.patronymic AS fio, " +
    " ch.name AS charm, " +
    " extract(year from age(c.birth_date)) as age " +
    " from client c join charm ch on (c.charm_id = ch.id) " +
    " where c.actual = 1 and ch.actual = 1 " +
    " and c.id = #{id}")
  ClientRecord getClientRecord(@Param("id") String id);


  @Update("update client_addr set ${fieldName} = #{value} where client = #{id} and type = 'fact'")
  void updateFirstAddressField(@Param("id") String id,
                               @Param("fieldName") String fieldName,
                               @Param("value") Object value);

  @Select("select street from client_addr where client = #{id} and actual = 1 union all" +
    " select house from client_addr where client = #{id} and actual = 1 union all" +
    " select flat from client_addr where client = #{id} and actual = 1")
  List<String> getFirstAddress(@Param("id") String id);
}
