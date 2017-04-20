trigger TypeFinderMultiFile on Account (before insert) {
    Integer int = 0; 
    TypeFinder.InnerClass1 cls1 = new TypeFinder.InnerClass1();
    cls1.methodWith2Params(int, cls1.innerStr1); // #findMethodFromInnerClassOfAnotherClass#
}
