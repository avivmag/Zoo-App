using DAL;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using NegevZoo.Controllers;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Web.Http;

namespace ZooTests
{
    [TestClass]
    public class NotificationControllerBlIntegraionTests
    {
        private NotificationController NotificationController;

        #region SetUp and TearDown
        [TestInitialize]
        public void SetUp()
        {
            // The line below must be in every setup of each test. otherwise it will not be in a testing environment.
            ControllerBase.isTesting = true;
            NotificationController = new NotificationController();
        }

        [TestCleanup]
        public void EnclosureCleanUp()
        {
            DummyDB.CleanDb();
        }
        #endregion

        #region GetAllDevices
        [TestMethod]
        public void GetAllDevices()
        {
            Assert.AreEqual(2, NotificationController.GetAllDevices().Count());
        }
        #endregion

        #region updateDevice
        [TestMethod]
        public void UpdateDeviceStatusAddDeviceValidInput()
        {
            var devices = NotificationController.GetAllDevices();
            Assert.AreEqual(2, devices.Count());
            
            NotificationController.UpdateDevice("12345", true);

            devices = NotificationController.GetAllDevices();
            Assert.AreEqual(3, devices.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateDeviceStatusAddDeviceEmptyDeviceId()
        {
            var devices = NotificationController.GetAllDevices();
            Assert.AreEqual(2, devices.Count());

            NotificationController.UpdateDevice("", true);
        }

        [TestMethod]
        public void UpdateDeviceStatusUpdateDeviceValidInput()
        {
            var devices = NotificationController.GetAllDevices();
            Assert.AreEqual(2, devices.Count());

            NotificationController.UpdateDevice("123", true);

            devices = NotificationController.GetAllDevices();
            Assert.AreEqual(DateTime.Now, devices.Single(d => d.deviceId == "123").lastPing);
            Assert.AreEqual(2, devices.Count());

        }


        #endregion

        [TestMethod]
        public void SendRecEventNotification()
        {
            var enclosureController = new EnclosureController();

            var allRecEvents = enclosureController.GetRecurringEvents(3, 1);
            Assert.AreEqual(0, allRecEvents.Count());

            var currentTime = DateTime.Now;

            var recEve = new RecurringEvent
            {
                id          = default(int),
                enclosureId = 3,
                day         = (long)currentTime.DayOfWeek + 1,
                startTime   = new TimeSpan(currentTime.Hour,currentTime.Minute + 3,currentTime.Second),
                endTime     = new TimeSpan(currentTime.Hour, currentTime.Minute + 33, currentTime.Second),
                title       = "בדיקה",
                description = "קקי",
                language    = (long)Languages.he
            };

            enclosureController.UpdateRecurringEvent(recEve);

            allRecEvents = enclosureController.GetRecurringEvents(3, 1);
            Assert.AreEqual(1, allRecEvents.Count());

            var response = NotificationController.SendNotificationsOnlineRecurringEvents();
        }
    }
}