using DAL;
using BL;
using System;
using System.Threading.Tasks;
using System.Threading;
using System.ServiceProcess;
using System.ComponentModel;

namespace RecEventsNotifications
{
    public class LaunchRecEventsBase : System.ServiceProcess.ServiceBase
    {
        private ManualResetEvent shutdownEvent;
        private Thread thread;

        protected override void OnStart(string[] args)
        {
            // Create the threadstart object to wrap our delegate method.
            ThreadStart threadStart = new ThreadStart(this.Run);

            // create the manual reset event and
            // set it to an initial state of unsignaled
            shutdownEvent           = new ManualResetEvent(false);

            // create the worker thread
            thread                  = new Thread(threadStart);

            // go ahead and start the worker thread
            thread.Start();

            // call the base class so it has a chance
            // to perform any work it needs to
            base.OnStart(args);
        }

        protected override void OnStop()
        {
            // Signal the event to shutdown.
            shutdownEvent.Set();

            // Wait for the thread to stop giving it 2 seconds.
            thread.Join(2000);

            // Call the base class stop.
            base.OnStop();
        }

        private void Run()
        {
            // Set the starting time to zero and the time between the operation to 10 minutes.
            var periodTimeSpan  = TimeSpan.FromSeconds(10);
            var startTimeSpan   = TimeSpan.Zero;

            // Create an IPC wait handle with a unique identifier.
            var waitHandle = new EventWaitHandle(false, EventResetMode.AutoReset, "CF2D4313-33DE-489D-9721-6AFF69841DEA", out bool createdNew);
            var signaled = false;

            // If the handle was already there, inform the other process to exit itself.
            // Afterwards we'll also die.
            if (!createdNew)
            {
                Logger.LoggerRec.GetLoggerRecInstance().WriteLine("Inform other process to stop.");
                waitHandle.Set();
                Logger.LoggerRec.GetLoggerRecInstance().WriteLine("Informer exited.");

                return;
            }

            // Start a another thread that does something every 10 seconds.
            var timer = new Timer((e) =>
            {
                Logger.LoggerRec.GetLoggerRecInstance().WriteLine("Timer Init.");
                //setting the task to be the sending function
                try
                {
                    SendRecNotification();
                }
                catch (Exception exp)
                {
                    Logger.LoggerRec.GetLoggerRecInstance().WriteLine(exp.Message);
                    Logger.LoggerRec.GetLoggerRecInstance().WriteLine(exp.InnerException?.Message);
                }
            }, null, startTimeSpan, periodTimeSpan);

            Logger.LoggerRec.GetLoggerRecInstance().WriteLine("Entering wait state.");

            // Wait if someone tells us to die or do every five seconds something else.
            do
            {
                Logger.LoggerRec.GetLoggerRecInstance().WriteLine("Waiting one more minute.");
                signaled = waitHandle.WaitOne(TimeSpan.FromMinutes(1));
            } while (!signaled);

            Logger.LoggerRec.GetLoggerRecInstance().WriteLine("Wait state ended. killing process...");

            // The above loop with an interceptor could also be replaced by an endless waiter
            //waitHandle.WaitOne();

            Logger.LoggerRec.GetLoggerRecInstance().WriteLine("Got signal to kill myself.");
        }

        private void SendRecNotification()
        {
            Logger.LoggerRec.GetLoggerRecInstance().WriteLine("Trying to send recurring notification.");
            using (var context = new ZooContext(false))
            {
                Logger.LoggerRec.GetLoggerRecInstance().WriteLine("Calling Web API to send notifications about RecurringEvents");
                context.SendNotificationsOnlineDevicesRecurringEvents();
                Logger.LoggerRec.GetLoggerRecInstance().WriteLine("Finished notiifcation send call.");
            }
        }
    }

    public class LaunchRecEvents : LaunchRecEventsBase
    {
        public LaunchRecEvents()
        {
            this.ServiceName = "RecurringEvents";
        }

        /// <SUMMARY>
        /// Set things in motion so your service can do its work.
        /// </SUMMARY>
        protected override void OnStart(string[] args)
        {
            base.OnStart( args );
        }
 
        /// <SUMMARY>
        /// Stop this service.
        /// </SUMMARY>
        protected override void OnStop()
        {
            base.OnStop();
        }

        static void Main(string[] args)
        {
            // we'll go ahead and create an array so that we
            // can add the different services that
            // we'll create over time.
            ServiceBase[] servicesToRun;

            // to create a new instance of a new service,
            // just add it to the list of services 
            // specified in the ServiceBase array constructor.
            servicesToRun = new ServiceBase[] { new LaunchRecEvents() };

            // now run all the service that we have created.
            // This doesn't actually cause the services
            // to run but it registers the services with the
            // Service Control Manager so that it can
            // when you start the service the SCM will call
            // the OnStart method of the service.
            ServiceBase.Run(servicesToRun);
        }
    }

    [RunInstaller(true)]
    public class RecurringEventsInstaller : System.Configuration.Install.Installer
    {
        public RecurringEventsInstaller()
        {
            ServiceProcessInstaller process = new ServiceProcessInstaller();

            process.Account = ServiceAccount.LocalSystem;

            ServiceInstaller serviceAdmin = new ServiceInstaller();

            serviceAdmin.StartType        = ServiceStartMode.Manual;
            serviceAdmin.ServiceName    = "RecurringEventsNotifications";
            serviceAdmin.DisplayName    = "Recurring Events Notifications Service";
            
            // Microsoft didn't add the ability to add a
            // description for the services we are going to install
            // To work around this we'll have to add the
            // information directly to the registry but I'll leave
            // this exercise for later.


            // now just add the installers that we created to our
            // parents container, the documentation
            // states that there is not any order that you need to
            // worry about here but I'll still
            // go ahead and add them in the order that makes sense.
            Installers.Add( process );
            Installers.Add( serviceAdmin );
        }
    }
}
