using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DAL.Models
{
    public class MapSettingsResult
    {
        public string PointsPath { get; set; }
        public double Longitude { get; set; }
        public double Latitude { get; set; }
        public int ZooPointX { get; set; }
        public int ZooPointY { get; set; }
        public double XLongitudeRatio { get; set; }
        public double YLatitudeRatio { get; set; }
        public double SinAlpha { get; set; }
        public double CosAlpha { get; set; }
        public double MinLatitude { get; set; }
        public double MaxLatitude { get; set; }
        public double MinLongitude { get; set; }
        public double MaxLongitude { get; set; }

        public Route[] Routes { get; set; }
    }
}
