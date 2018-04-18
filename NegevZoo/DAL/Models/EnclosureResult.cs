using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DAL.Models
{
    public class EnclosureResult
    {
        public long Id { get; set; }
        public string Name { get; set; }
        public string Story { get; set; }
        public double? MarkerLongtitude { get; set; }
        public double? MarkerLatitude { get; set; }
        public string MarkerIconUrl { get; set; }
        public string PictureUrl { get; set; }
        public long Language { get; set; }
        public YoutubeVideoUrl[] Videos { get; set; }
        public EnclosurePicture[] Pictures { get; set; }
        public RecurringEventsResult[] RecEvents {get;set;}
    }
}
