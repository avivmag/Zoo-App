namespace DAL.Models
{
    public class RecurringEventsResult
    {
        public long Id { get; set; }
        public string Title { get; set; }
        public string Description { get; set; }
        public int EnclosureId { get; set; }
        public long Language { get; set; }
        public long StartTime { get; set; }
        public long EndTime { get; set; }
    }
}