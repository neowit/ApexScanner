public virtual class TypeFinder {
    
    private Boolean bool; //result of findClassVariableTypeWithThis
    private TypeFinder classInstance;

    public virtual void methodToCall() {
        System.debug('Writing some text.');
    }

    public virtual Double caller1() {
        methodToCall();
        return .05;
    } 

    public void findMethodType(String str) {
        Integer int = 0; 
        methodWith2Params(int, str); //#findMethodType#  #findMethodType_int_str#
        
        methodWith3Params(int, str, this.bool); //#findMethodType_int_str_bool#
    } 

    private M2Type methodWith2Params(Integer i, String s) { // #result of findMethodType
        String str;
        Integer int;
        
    }

    private M2Type methodWith2Params(String s1, String s2) { // #result of findMethodType
        String str;
        Integer int;
        
    }
    private M3Type methodWith3Params(Integer i, String s2, Boolean b1) { // #result of findMethodType_int_str_bool#
        String str;
        Integer int;
    }

    private void findVarFromInnerClass() {
        InnerClass1 cls1 = new InnerClass1();
        String str = cls1.innerStr1; // #findVarFromInnerClass#
    }

    public class InnerClass1 {
        public String innerStr1;
        public M2Inner_I_S methodWith2Params(Integer i, String s2) {
        }
    }
}
