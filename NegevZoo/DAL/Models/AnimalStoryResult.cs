using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DAL.Models
{
    public class AnimalStoryResult
    {
        public long Id { get; set; }
        public string Name { get; set; }
        public string Story { get; set; }
        public long EncId { get; set; }
        public string PictureUrl { get; set; }
        public long Language { get; set; }
        public byte[] pictureData { get; set; }
    }
}
