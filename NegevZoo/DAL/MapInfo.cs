//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated from a template.
//
//     Manual changes to this file may cause unexpected behavior in your application.
//     Manual changes to this file will be overwritten if the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace DAL
{
    using System;
    using System.Collections.Generic;
    
    public partial class MapInfo
    {
        public int id { get; set; }
        public string pointspath { get; set; }
        public double longitude { get; set; }
        public double latitude { get; set; }
        public int zooPointX { get; set; }
        public int zooPointY { get; set; }
        public double xLongitudeRatio { get; set; }
        public double sinAlpha { get; set; }
        public double cosAlpha { get; set; }
        public double minLatitude { get; set; }
        public double maxLatitude { get; set; }
        public double minLongitude { get; set; }
        public double maxLongitude { get; set; }
        public double yLatitudeRatio { get; set; }
    }
}