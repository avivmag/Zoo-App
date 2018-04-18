using DAL;
using BL;
using System;
using System.Threading.Tasks;

namespace RecEventsNotifications
{
    public class RecEventsNotifications
    {
        public static void Main(string[] args)
        {
            new SentRecurringEventNotification().Run();
        }
        
        internal class SentRecurringEventNotification
        {
            ZooContext context;

            internal void Run()
            {
                //set the starting time to zero and the time between the operation to 10 minuits
                var startTimeSpan = TimeSpan.Zero;
                var periodTimeSpan = TimeSpan.FromMinutes(10);
                    
                //starting the timer with the task.
                var timer = new System.Threading.Timer((e) =>
                {
                    //setting the task to be the sending function
                    Task.Factory.StartNew(() => SendRecNotification());
                }, null, startTimeSpan, periodTimeSpan);
                
            }

            private void SendRecNotification()
            {
                using (context = new ZooContext(false))
                {
                    Logger.GetInstance().WriteLine("Calling Web API to send notifications about RecurringEvents");
                    context.SendNotificationsOnlineDevicesRecurringEvents();
                    Logger.GetInstance().WriteLine("Package Sent");
                }
            }
        }
    }
}
