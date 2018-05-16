using DAL;
using BL;
using System;
using System.Threading.Tasks;
using System.Threading;

namespace RecEventsNotifications
{
    public class LaunchRecEvents
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
                var periodTimeSpan = TimeSpan.FromMinutes(10);
                var startTimeSpan = TimeSpan.Zero;

                //starting the timer with the task.
                var timer = new Timer((e) =>
                {
                    //setting the task to be the sending function
                    Task.Factory.StartNew(() => SendRecNotification());
                }, null, startTimeSpan, periodTimeSpan);

                //keeping the runing alive so the process won't die.
                while (true) { }
            }

            private void SendRecNotification()
            {
                using (context = new ZooContext(false))
                {
                    Logger.GetInstance(false).WriteLine("Calling Web API to send notifications about RecurringEvents");
                    context.SendNotificationsOnlineDevicesRecurringEvents();
                    Logger.GetInstance(false).WriteLine("Package Sent");
                }
            }
        }
    }
}
