package kz.greetgo.sandbox.db.register_impl.migration.enums;

public enum TmpTableName {
  TMP_CLIENT("#{{TMP_CLIENT}}"),
  TMP_ADDRESS("#{{TMP_ADDRESS}}"),
  TMP_PHONE("#{{TMP_PHONE}}"),
  TMP_ACCOUNT("#{{TMP_ACCOUNT}}"),
  TMP_TRANSACTION("#{{TMP_TRANSACTION}}");

  public String code;

  @SuppressWarnings("UnnecessaryEnumModifier")
  private TmpTableName(String code) {
    this.code = code;
  }

}
