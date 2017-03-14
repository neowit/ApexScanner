global virtual class PropertyVisibility {
   // X is private for read and public for write
   public integer X { private get; set; }
   // Y can be globally read but only written within a class
   global integer Y { get; public set; }
   // Z can be read within the class but only subclasses can set it
   public integer Z { get; protected set; }
}
