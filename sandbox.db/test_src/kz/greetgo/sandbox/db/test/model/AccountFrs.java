package kz.greetgo.sandbox.db.test.model;


import java.util.ArrayList;
import java.util.List;

public class AccountFrs {
  public String id;
  public String type;
  public String client_id;
  public String account_number;
  public String registered_at;
  public String error;
  public List<Transaction> transactionList = new ArrayList<>();


  @SuppressWarnings("SimplifiableIfStatement")
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof AccountFrs)) return false;

    AccountFrs account = (AccountFrs) o;

    if (client_id != null ? !client_id.equals(account.client_id) : account.client_id != null) return false;
    if (account_number != null ? !account_number.equals(account.account_number) : account.account_number != null)
      return false;
    return registered_at != null ? registered_at.equals(account.registered_at) : account.registered_at == null;
  }

  @Override
  public int hashCode() {
    int result = client_id != null ? client_id.hashCode() : 0;
    result = 31 * result + (account_number != null ? account_number.hashCode() : 0);
    result = 31 * result + (registered_at != null ? registered_at.hashCode() : 0);
    return result;
  }
}