using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DAL.Models
{
    public class MapSettingsResult
    {
        double ZooLocationLatitude { get; set; }
        double ZooLocationLongitude { get; set; }
        int ZooLocationX { get; set; }
        int ZooLocationY { get; set; }
        double XLongitudeRatio { get; set; }
        double YLatitudeRatio { get; set; }
        double SinAlpha { get; set; }
        double CosAlpha { get; set; }

        int MinX { get; set; }
        int MaxX { get; set; }
        int MinY { get; set; }
        int MaxY { get; set; }

        String points { get; set; }
    }
}
