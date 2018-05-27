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
        private ManualResetEvent m_shutdownEvent;
        private Thread m_thread;

        protected override void OnStart(string[] args)
        {
            // create our threadstart object to wrap our delegate method
            ThreadStart ts = new ThreadStart(this.Run);

            // create the manual reset event and
            // set it to an initial state of unsignaled
            m_shutdownEvent = new ManualResetEvent(false);

            // create the worker thread
            m_thread = new Thread(ts);

            // go ahead and start the worker thread
            m_thread.Start();

            // call the base class so it has a chance
            // to perform any work it needs to
            base.OnStart(args);
        }

        protected override void OnStop()
        {
            // signal the event to shutdown
            m_shutdownEvent.Set();

            // wait for the thread to stop giving it 10 seconds
            m_thread.Join(10000);

            // call the base class 
            base.OnStop();
        }

        private void Run()
        {
            //set the starting time to zero and the time between the operation to 10 minuits
            var periodTimeSpan = TimeSpan.FromMinutes(10);
            var startTimeSpan = TimeSpan.Zero;

            // Create an IPC wait handle with a unique identifier.
            var waitHandle = new EventWaitHandle(false, EventResetMode.AutoReset, "CF2D4313-33DE-489D-9721-6AFF69841DEA", out bool createdNew);
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
                signaled = waitHandle.WaitOne(TimeSpan.FromMinutes(5));
            } while (!signaled);

            // The above loop with an interceptor could also be replaced by an endless waiter
            //waitHandle.WaitOne();

            Logger.GetInstance(false).WriteLine("Got signal to kill myself.");
        }

        private void SendRecNotification()
        {
            using (var context = new ZooContext(false))
            {
                Logger.GetInstance(false).WriteLine("Calling Web API to send notifications about RecurringEvents");
                context.SendNotificationsOnlineDevicesRecurringEvents();
                Logger.GetInstance(false).WriteLine("Package Sent");
            }
        }

        //public static void Main(string[] args)
        //{
        //    new SentRecurringEventNotification().Run();
        //}
        
        //internal class SentRecurringEventNotification
        //{
        //    ZooContext context;

        //    internal void Run()
        //    {
        //        //set the starting time to zero and the time between the operation to 10 minuits
        //        var periodTimeSpan = TimeSpan.FromMinutes(10);
        //        var startTimeSpan = TimeSpan.Zero;

        //        // Create an IPC wait handle with a unique identifier.
        //        bool createdNew;
        //        var waitHandle = new EventWaitHandle(false, EventResetMode.AutoReset, "CF2D4313-33DE-489D-9721-6AFF69841DEA", out createdNew);
        //        var signaled = false;

        //        // If the handle was already there, inform the other process to exit itself.
        //        // Afterwards we'll also die.
        //        if (!createdNew)
        //        {
        //            Logger.GetInstance(false).WriteLine("Inform other process to stop.");
        //            waitHandle.Set();
        //            Logger.GetInstance(false).WriteLine("Informer exited.");

        //            return;
        //        }

        //        // Start a another thread that does something every 10 seconds.
        //        var timer = new Timer((e) =>
        //            {
        //                //setting the task to be the sending function
        //                Task.Factory.StartNew(() => SendRecNotification());
        //            }, null, startTimeSpan, periodTimeSpan);

        //        // Wait if someone tells us to die or do every five seconds something else.
        //        do
        //        {
        //            signaled = waitHandle.WaitOne(TimeSpan.FromMinutes(5));
        //        } while (!signaled);

        //        // The above loop with an interceptor could also be replaced by an endless waiter
        //        //waitHandle.WaitOne();

        //        Logger.GetInstance(false).WriteLine("Got signal to kill myself.");
        //    }

        //    private void SendRecNotification()
        //    {
        //        using (context = new ZooContext(false))
        //        {
        //            Logger.GetInstance(false).WriteLine("Calling Web API to send notifications about RecurringEvents");
        //            context.SendNotificationsOnlineDevicesRecurringEvents();
        //            Logger.GetInstance(false).WriteLine("Package Sent");
        //        }
        //    }
        //}
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
    public class SpadesInstaller : System.Configuration.Install.Installer
    {
        public SpadesInstaller()
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
