using Backend.Models;
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
    public class ZooInfoControllerTest
    {
        private ZooInfoController ZooInfoController;
        private int nonExistantLangauge;

        [TestInitialize]
        public void SetUp()
        {
            // The line below must be in every setup of each test. otherwise it will not be in a testing environment.
            ControllerBase.isTesting = true;
            ZooInfoController = new ZooInfoController();
            nonExistantLangauge = 100;
        }

        [TestMethod]
        public void GetAllWallFeedLangHe()
        {
            Assert.AreEqual(32, ZooInfoController.GetAllFeeds(1).Count());
        }

        [TestMethod]
        public void GetAllWallFeedLangEng()
        {
            Assert.AreEqual(32, ZooInfoController.GetAllFeeds(2).Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetAllWallFeedLanguageNotExist()
        {
            Assert.AreEqual(ZooInfoController.GetAllFeeds(nonExistantLangauge).Count(), 0);
        }

        [TestMethod]
        public void GetAllPricesLangHe()
        {
            var prices = ZooInfoController.GetAllPrices(1);
            Assert.IsInstanceOfType(prices, typeof(Price[]));
            
            Assert.AreEqual(5, prices.Count());
            Assert.IsTrue(prices.Any(p=>p.Population=="מבוגר"));
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetAllPricesLanguageNotExist()
        {
            var prices = ZooInfoController.GetAllPrices(nonExistantLangauge);
            
            Assert.AreEqual(prices.Count(), 0);
        }

        [TestMethod]
        public void GetAllPricesLangEng()
        {
            var prices = ZooInfoController.GetAllPrices(2);
            Assert.IsInstanceOfType(prices, typeof(Price[]));

            Assert.AreEqual(5, prices.Count());
            Assert.IsTrue(prices.Any(p => p.Population == "Adult"));
        }

        [TestMethod]
        public void GetAboutUsLangaugeNotExist()
        {
            var aboutUs = ZooInfoController.GetZooAboutInfo(nonExistantLangauge);

            Assert.AreEqual(aboutUs.Count(), 0);
        }

        [TestMethod]
        public void GetAboutUs()
        {
            var aboutUs = ZooInfoController.GetZooAboutInfo(1);
            Assert.IsInstanceOfType(aboutUs, typeof(ZooInfoController.AboutUsResult[]));

            Assert.AreEqual(aboutUs.Count(), 1);
        }
    }
}
