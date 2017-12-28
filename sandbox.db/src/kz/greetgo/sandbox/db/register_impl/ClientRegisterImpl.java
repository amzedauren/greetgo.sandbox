package kz.greetgo.sandbox.db.register_impl;

import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.sandbox.controller.model.*;
import kz.greetgo.sandbox.controller.register.ClientRegister;
import kz.greetgo.sandbox.db.dao.ClientDao;
import kz.greetgo.sandbox.db.register_impl.jdbc.GetClientList;
import kz.greetgo.sandbox.db.register_impl.jdbc.GetClientListSize;
import kz.greetgo.sandbox.db.register_impl.jdbc.GetClientPhones;
import kz.greetgo.sandbox.db.report.ClientRecord.ClientRecordListReportViewXslx;
import kz.greetgo.sandbox.db.util.JdbcSandbox;
import kz.greetgo.util.RND;

import java.io.OutputStream;
import java.util.Date;
import java.util.List;

@Bean
public class ClientRegisterImpl implements ClientRegister {
  public BeanGetter<ClientDao> clientDao;

  public BeanGetter<JdbcSandbox> jdbc;

  @Override
  public long getSize(ClientListRequest clientListRequest) {
    clientListRequest.filterByFio = clientListRequest.filterByFio.trim();
    return jdbc.get().execute(new GetClientListSize(clientListRequest));
  }

  @Override
  public List<ClientRecord> getList(ClientListRequest clientListRequest) {
    return jdbc.get().execute(new GetClientList(clientListRequest));
  }


  @Override
  public ClientDetails getClient(String id) {
    ClientDetails ret = new ClientDetails();

    if (id != null && !"".equals(id)) {
      ret = clientDao.get().loadDetails(id);
      ret.factAddress = clientDao.get().getFactAddress(id);
      ret.regAddress = clientDao.get().getRegAddress(id);
      ClientPhones phones = new ClientPhones();

      ret.phones = jdbc.get().execute(new GetClientPhones(id));
    }
    ret.charms = clientDao.get().loadCharmList();

    return ret;
  }

  public BeanGetter<IdGenerator> idGen;

  @Override
  public ClientRecord saveClient(ClientToSave clientToSave) {
    java.sql.Date dateOfBirth = java.sql.Date.valueOf(clientToSave.dateOfBirth);

    clientToSave.gender = clientToSave.gender.toLowerCase();

    if (clientToSave.id != null) {

      //Khamit mojno li sdelat odnim query - 1
      clientDao.get().updateClientField(clientToSave.id, "name", clientToSave.name);
      clientDao.get().updateClientField(clientToSave.id, "surname", clientToSave.surname);
      clientDao.get().updateClientField(clientToSave.id, "patronymic", clientToSave.patronymic);
      clientDao.get().updateClientField(clientToSave.id, "charm_id", clientToSave.charmId);
      clientDao.get().updateClientField(clientToSave.id, "birth_date", dateOfBirth);
      clientDao.get().updateClientField(clientToSave.id, "current_gender", clientToSave.gender);
      clientDao.get().updateClientField(clientToSave.id, "actual", 1);

      clientDao.get().updateAddressField(clientToSave.id, "fact", "street", clientToSave.factAddress.street);
      clientDao.get().updateAddressField(clientToSave.id, "fact", "house", clientToSave.factAddress.house);
      clientDao.get().updateAddressField(clientToSave.id, "fact", "flat", clientToSave.factAddress.flat);

      clientDao.get().updateAddressField(clientToSave.id, "reg", "street", clientToSave.regAddress.street);
      clientDao.get().updateAddressField(clientToSave.id, "reg", "house", clientToSave.regAddress.house);
      clientDao.get().updateAddressField(clientToSave.id, "reg", "flat", clientToSave.regAddress.flat);

      clientDao.get().deleteClientPhone(clientToSave.id);
      clientDao.get().insertClientPhone(
        clientToSave.id,
        clientToSave.phones.home,
        "home"
      );

      clientDao.get().insertClientPhone(
        clientToSave.id,
        clientToSave.phones.work,
        "work"
      );

      for (String s : clientToSave.phones.mobile)
        clientDao.get().insertClientPhone(
          clientToSave.id,
          s,
          "mobile"
        );

    } else {
      clientToSave.id = idGen.get().newId();
      clientDao.get().insertClient(
        clientToSave.id,
        clientToSave.name,
        clientToSave.surname,
        clientToSave.patronymic,
        dateOfBirth,
        clientToSave.gender,
        clientToSave.charmId);

      clientDao.get().insertClientAddress(
        clientToSave.id,
        clientToSave.factAddress.street,
        clientToSave.factAddress.house,
        clientToSave.factAddress.flat,
        "fact");

      clientDao.get().insertClientAddress(
        clientToSave.id,
        clientToSave.regAddress.street,
        clientToSave.regAddress.house,
        clientToSave.regAddress.flat,
        "reg");

      clientDao.get().insertClientPhone(
        clientToSave.id,
        clientToSave.phones.home,
        "home"
      );

      clientDao.get().insertClientPhone(
        clientToSave.id,
        clientToSave.phones.work,
        "work"
      );

      for (String s : clientToSave.phones.mobile)
        clientDao.get().insertClientPhone(
          clientToSave.id,
          s,
          "mobile"
        );

      clientDao.get().insertClientAccount(
        idGen.get().newId(),
        clientToSave.id,
        0,
        RND.intStr(10),
        new Date()
      );

    }

    ClientRecord rec = clientDao.get().getClientRecord(clientToSave.id);

    return rec;
  }

  @Override
  public void deleteClient(String id) {
    if (id == null) return;

    clientDao.get().deleteClient(id);
    clientDao.get().deleteClientAddress(id);
    clientDao.get().deleteClientPhone(id);
    clientDao.get().deleteClientAccount(id);
  }


  @Override
  public void download(ClientListRequest clientListRequest, OutputStream outputStream, String contentType, String personId) throws Exception {

    if (contentType.contains("pdf")) {

    } else {
      ClientRecordListReportViewXslx view = new ClientRecordListReportViewXslx(outputStream);

      try {
        view.start(new Date());

        clientListRequest.count = 0;

        List<ClientRecord> rec = jdbc.get().execute(new GetClientList(clientListRequest));

        for (ClientRecord r : rec) {
          view.append(r);
        }

      } finally {
        view.finish();
      }
    }
  }

}
