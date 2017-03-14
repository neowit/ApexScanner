public class StaticProperty {
    public static integer StaticMember;
    public integer NonStaticMember;
    public static integer MyGoodStaticProp {
        get{return MyGoodStaticProp;}
    }
    // The following produces a system error
    // public static integer MyBadStaticProp { return NonStaticMember; }
    public integer MyGoodNonStaticProp {
        get{return NonStaticMember;}
    } 
}
