package kz.greetgo.sandbox.db.stand.model;

import kz.greetgo.sandbox.controller.model.CharmRecord;
import kz.greetgo.sandbox.controller.model.ClientDetails;
import kz.greetgo.sandbox.controller.model.ClientRecord;

public class ClientDot {

  public String id;
  public String name;
  public String surname;
  public String patronymic;
  public String temper;
  public String dateOfBirth;
  public long balance;
  ///////////DETAILS
  public String gender;
  public String address;
  public String address2;
  public String phone;

  public ClientRecord toClientRecord(){
    ClientRecord rec = new ClientRecord();
    rec.id = this.id;
    rec.fio = this.surname + " " + this.name + " " + this.patronymic;
    rec.charm = this.temper;
    int year = Integer.parseInt(this.dateOfBirth.trim().substring(0, 4));
    rec.age = 2017 - year;
    rec.totalAccountBalance = this.balance;
    rec.maxAccountBalance = this.balance;
    rec.minAccountBalance = this.balance;
    return rec;
  }

  public ClientDetails toClientDetails(){
    ClientDetails rec = new ClientDetails();

    rec.id = this.id;
    rec.name = this.name;
    rec.surname = this.surname;
    rec.patronymic = this.patronymic;
    rec.dateOfBirth = String.valueOf(this.dateOfBirth);
    rec.gender = this.gender;
    rec.charm = this.temper;


    CharmRecord charmRecord = new CharmRecord();
    CharmRecord charmRecord2 = new CharmRecord();
    CharmRecord charmRecord3 = new CharmRecord();
    charmRecord.id = 1;
    charmRecord2.id = 2;
    charmRecord3.id = 3;
    charmRecord.name = "Хорош";
    charmRecord2.name = "Плохо";
    charmRecord3.name = "Отлично";


    rec.charms.add(charmRecord);
    rec.charms.add(charmRecord2);
    rec.charms.add(charmRecord3);


    String[] splitPhones = this.phone.split("\\s+");
    for(String s: splitPhones)rec.phones.add(s);

    String[] splitAddress = this.address.split("\\s+");
    for(String s: splitAddress)rec.firstAddress.add(s);

    return rec;
  }

}
