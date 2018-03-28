using DAL;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using NegevZoo.Controllers;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ZooTests
{
    [TestClass]
    public class NotificationControllerTests
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
    }
}