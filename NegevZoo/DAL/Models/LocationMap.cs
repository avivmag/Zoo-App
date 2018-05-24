using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DAL.Models
{
    public class LocationMap
    {
        public double Latitude { get; set; }
        public double Longitude { get; set; }

        public LocationMap(double latitude, double longitude)
        {
            Latitude    = latitude;
            Longitude   = longitude;
        }

        public override bool Equals(object obj)
        {
            if (obj.GetType() != typeof(LocationMap))
            {
                return false;
            }

            LocationMap other = (LocationMap)obj;
            return other.Latitude == Latitude && other.Longitude == Longitude;
        }

        public override int GetHashCode()
        {
            var hashCode = -1416534245;
            hashCode = hashCode * -1521134295 + Latitude.GetHashCode();
            hashCode = hashCode * -1521134295 + Longitude.GetHashCode();
            return hashCode;
        }
    }
}
