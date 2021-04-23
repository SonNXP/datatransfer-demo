package datatransfer.event;

public enum IdentifierType {
    IdentifierType_V7(0),
    IdentifierType_Reserved_1(1),
    IdentifierType_Hashed(2),
    IdentifierType_Trip(3),
    IdentifierType_Personalization(4),
    IdentifierType_Debug(5),
    IdentifierType_Realtime(6),
    IdentifierType_Supplemental(7),
    IdentifierType_UID(8),
    IdentifierType_Reserved_9(9),
    IdentifierType_Reserved_10(10),
    IdentifierType_No_Identifier(255);

    private int idType;
    IdentifierType(int idType) {
        this.idType = idType;
    }

}
