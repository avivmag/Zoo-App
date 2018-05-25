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
        public int? MarkerX { get; set; }
        public int? MarkerY { get; set; }
        public int? MarkerClosestPointX { get; set; }
        public int? MarkerClosestPointY { get; set; }
        public string MarkerIconUrl { get; set; }
        public string PictureUrl { get; set; }
        public long Language { get; set; }
        public YoutubeVideoUrl[] Videos { get; set; }
        public EnclosurePicture[] Pictures { get; set; }
        public RecurringEventsResult[] RecEvents {get;set;}
    }
}
