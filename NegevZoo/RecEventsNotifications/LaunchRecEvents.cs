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
                var periodTimeSpan = TimeSpan.FromSeconds(45);
                var startTimeSpan = TimeSpan.Zero;

                // Create an IPC wait handle with a unique identifier.
                bool createdNew;
                var waitHandle = new EventWaitHandle(false, EventResetMode.AutoReset, "CF2D4313-33DE-489D-9721-6AFF69841DEA", out createdNew);
                var signaled = false;

                // If the handle was already there, inform the other process to exit itself.
                // Afterwards we'll also die.
                if (!createdNew)
                {
                    Logger.GetInstance(false).WriteLine("Inform other process to stop.");
                    waitHandle.Set();
                    Logger.GetInstance(false).WriteLine("Informer exited.");

                    return;
                }

                // Start a another thread that does something every 10 seconds.
                var timer = new Timer((e) =>
                    {
                        //setting the task to be the sending function
                        Task.Factory.StartNew(() => SendRecNotification());
                    }, null, startTimeSpan, periodTimeSpan);

                // Wait if someone tells us to die or do every five seconds something else.
                do
                {
                    signaled = waitHandle.WaitOne(TimeSpan.FromSeconds(30));
                } while (!signaled);

                // The above loop with an interceptor could also be replaced by an endless waiter
                //waitHandle.WaitOne();

                Logger.GetInstance(false).WriteLine("Got signal to kill myself.");
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
