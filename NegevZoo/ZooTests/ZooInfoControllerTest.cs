using Backend.Models;
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
    public class ZooInfoControllerTest
    {
        private ZooInfoController ZooInfoController;

        [TestInitialize]
        public void SetUp()
        {
            // The line below must be in every setup of each test. otherwise it will not be in a testing environment.
            ControllerBase.isTesting = true;
            ZooInfoController = new ZooInfoController();
        }

        [TestMethod]
        public void getAllWallFeed()
        {
            Assert.AreEqual(4, ZooInfoController.GetAllFeeds().Count());
        }

        [TestMethod]
        public void getAllWallFeedLangEng()
        {
            Assert.AreEqual(4, ZooInfoController.GetAllFeeds(2).Count());
        }

        [TestMethod]
        public void getAllPrices()
        {
            var prices = ZooInfoController.GetPrices();
            Assert.IsInstanceOfType(prices, typeof(Price[]));
            
            Assert.AreEqual(5, prices.Count());
            Assert.IsTrue(prices.Any(p=>p.Population=="מבוגר"));
        }

        [TestMethod]
        public void getAllPricesLangEng()
        {
            var prices = ZooInfoController.GetPrices(2);
            Assert.IsInstanceOfType(prices, typeof(Price[]));

            Assert.AreEqual(5, prices.Count());
            Assert.IsTrue(prices.Any(p => p.Population == "Adult"));
        }
    }
}
