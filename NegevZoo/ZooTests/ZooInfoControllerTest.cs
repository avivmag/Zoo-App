using Backend;
using Backend.Models;
using BL;
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

        #region SetUp and TearDown
        [TestInitialize]
        public void SetUp()
        {
            // The line below must be in every setup of each test. otherwise it will not be in a testing environment.
            ControllerBase.isTesting = true;
            ZooInfoController = new ZooInfoController();
            nonExistantLangauge = 100;
        }

        [TestCleanup]
        public void EnclosureCleanUp()
        {
            ZooContext.CleanDb();
        }
        #endregion

        #region feeds

        #region getAllWallFeed
        [TestMethod]
        public void GetAllWallFeedLangHe()
        {
            Assert.AreEqual(16, ZooInfoController.GetAllFeeds(1).Count());
        }

        [TestMethod]
        public void GetAllWallFeedLangEng()
        {
            Assert.AreEqual(16, ZooInfoController.GetAllFeeds(2).Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetAllWallFeedLanguageNotExist()
        {
            Assert.AreEqual(ZooInfoController.GetAllFeeds(nonExistantLangauge).Count(), 0);
        }

        #endregion

        #region updateFeedWall
        [TestMethod]
        public void UpdateWallFeedAddFeedValidTest()
        {
            var feeds = ZooInfoController.GetAllFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());

            var feed = new WallFeed
            {
                Id = default(int),
                Created = new DateTime(2018, 03, 14),
                Info = "Another Kaki appears in the zoo",
                Language =(int)Languages.en
            };

            ZooInfoController.UpdateWallFeed(feed);

            feeds = ZooInfoController.GetAllFeeds((int)Languages.en);
            Assert.AreEqual(17, feeds.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateWallFeedAddFeedInfoAlreadyExists()
        {
            var feeds = ZooInfoController.GetAllFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());

            var feed = new WallFeed
            {
                Id = default(int),
                Created = new DateTime(2018, 03, 05),
                Info = "Purim Events",
                Language = (int)Languages.en
            };

            ZooInfoController.UpdateWallFeed(feed);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateWallFeedLanguageDoesntExists()
        {
            var feeds = ZooInfoController.GetAllFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());

            var feed = new WallFeed
            {
                Id = default(int),
                Created = new DateTime(2018, 03, 05),
                Info = "Purim Events",
                Language = nonExistantLangauge
            };

            ZooInfoController.UpdateWallFeed(feed);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateWallFeedWrongCreationDate()
        {
            var feeds = ZooInfoController.GetAllFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());

            var feed = new WallFeed
            {
                Id = default(int),
                Created = new DateTime(2019, 03, 05),
                Info = "Purim Events",
                Language = (int)Languages.en
            };

            ZooInfoController.UpdateWallFeed(feed);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateWallFeedWrongInfoEmptySpaces()
        {
            var feeds = ZooInfoController.GetAllFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());

            var feed = new WallFeed
            {
                Id = default(int),
                Created = new DateTime(2018, 03, 05),
                Info = "   ",
                Language = (int)Languages.en
            };

            ZooInfoController.UpdateWallFeed(feed);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateWallFeedWrongInfoEmpty()
        {
            var feeds = ZooInfoController.GetAllFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());

            var feed = new WallFeed
            {
                Id = default(int),
                Created = new DateTime(2018, 03, 05),
                Info = "",
                Language = (int)Languages.en
            };

            ZooInfoController.UpdateWallFeed(feed);
        }

        [TestMethod]
        public void UpdateWallFeedValidInput()
        {
            var feeds = ZooInfoController.GetAllFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());

            var feed = new WallFeed
            {
                Id = 1,
                Created = new DateTime(2018, 03, 05),
                Info = "Purim is back again!",
                Language = (int)Languages.en
            };

            ZooInfoController.UpdateWallFeed(feed);

            feeds = ZooInfoController.GetAllFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());
            Assert.IsTrue(feeds.Any(f => f.Info == feed.Info));
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateWallFeedInfoAlreadyExists()
        {
            var feeds = ZooInfoController.GetAllFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());

            var feed = new WallFeed
            {
                Id = 1,
                Created = new DateTime(2018, 03, 05),
                Info = "Sukut Events",
                Language = (int)Languages.en
            };

            ZooInfoController.UpdateWallFeed(feed);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateWallFeedIdDoesntExists()
        {
            var feeds = ZooInfoController.GetAllFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());

            var feed = new WallFeed
            {
                Id = -4,
                Created = new DateTime(2018, 03, 05),
                Info = "Purim is back again!",
                Language = (int)Languages.en
            };

            ZooInfoController.UpdateWallFeed(feed);
        }
        #endregion

        #region deleteWallFeed
        [TestMethod]
        public void DeleteWallFeedValidInput()
        {
            var feeds = ZooInfoController.GetAllFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());

            var firstFeed = feeds.SingleOrDefault(f => f.Id == 1);
            Assert.IsNotNull(firstFeed);

            ZooInfoController.DeleteWallFeed(firstFeed.Id);

            feeds = ZooInfoController.GetAllFeeds((int)Languages.en);
            Assert.AreEqual(15, feeds.Count());

            feeds = ZooInfoController.GetAllFeeds((int)Languages.he);
            Assert.AreEqual(16, feeds.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void DeleteWallFeedIdDoesntExists()
        {
            var feeds = ZooInfoController.GetAllFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());

            ZooInfoController.DeleteWallFeed(-4);
        }
        #endregion

        #endregion

        #region prices

        #region getAllPrices
        [TestMethod]
        public void GetAllPricesLangHe()
        {
            var prices = ZooInfoController.GetAllPrices(1);
            Assert.IsInstanceOfType(prices, typeof(Price[]));

            Assert.AreEqual(5, prices.Count());
            Assert.IsTrue(prices.Any(p => p.Population == "מבוגר"));
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
        [ExpectedException(typeof(HttpResponseException))]
        public void GetAllPricesLanguageNotExist()
        {
            var prices = ZooInfoController.GetAllPrices(nonExistantLangauge);

            Assert.AreEqual(prices.Count(), 0);
        }
        #endregion

        #region updatePrices
        [TestMethod]
        public void UpdatePriceAddPriceValidTest()
        {
            var prices = ZooInfoController.GetAllPrices((int)Languages.en);
            Assert.AreEqual(5, prices.Count());

            var price = new Price
            {
                Id = default(int),
                Population = "Veteran",
                PricePop = 15.5,
                Language = (int)Languages.en
            };

            ZooInfoController.UpdatePrice(price);

            prices = ZooInfoController.GetAllPrices((int)Languages.en);
            Assert.AreEqual(6, prices.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdatePriceAddPricePopulationAlreadyExists()
        {
            var prices = ZooInfoController.GetAllPrices((int)Languages.en);
            Assert.AreEqual(5, prices.Count());

            var price = new Price
            {
                Id = default(int),
                Population = "Adult",
                PricePop = 15.5,
                Language = (int)Languages.en
            };

            ZooInfoController.UpdatePrice(price);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdatePriceWrongLang()
        {
            var prices = ZooInfoController.GetAllPrices((int)Languages.en);
            Assert.AreEqual(5, prices.Count());

            var price = new Price
            {
                Id = default(int),
                Population = "Veteran",
                PricePop = 15.5,
                Language = nonExistantLangauge
            };

            ZooInfoController.UpdatePrice(price);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdatePriceWrongPopulationEmptySpaces()
        {
            var prices = ZooInfoController.GetAllPrices((int)Languages.en);
            Assert.AreEqual(5, prices.Count());

            var price = new Price
            {
                Id = default(int),
                Population = "   ",
                PricePop = 15.5,
                Language = (int)Languages.en
            };

            ZooInfoController.UpdatePrice(price);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdatePriceWrongPopulationEmpty()
        {
            var prices = ZooInfoController.GetAllPrices((int)Languages.en);
            Assert.AreEqual(5, prices.Count());

            var price = new Price
            {
                Id = default(int),
                Population = "",
                PricePop = 15.5,
                Language = (int)Languages.en
            };

            ZooInfoController.UpdatePrice(price);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdatePriceWrongPrice()
        {
            var prices = ZooInfoController.GetAllPrices((int)Languages.en);
            Assert.AreEqual(5, prices.Count());

            var price = new Price
            {
                Id = default(int),
                Population = "Veteran",
                PricePop = -15.5,
                Language = (int)Languages.en
            };

            ZooInfoController.UpdatePrice(price);
        }

        [TestMethod]
        public void UpdatePriceValidTest()
        {
            var prices = ZooInfoController.GetAllPrices((int)Languages.en);
            Assert.AreEqual(5, prices.Count());

            var price = new Price
            {
                Id = 9,
                Population = "Student",
                PricePop = 10,
                Language = (int)Languages.en
            };
            var oldPrice = price.PricePop;
            price.PricePop = 20;

            ZooInfoController.UpdatePrice(price);

            prices = ZooInfoController.GetAllPrices((int)Languages.en);
            Assert.AreEqual(5, prices.Count());
            Assert.IsTrue(prices.Any(p => p.Population == price.Population && p.PricePop == price.PricePop));
            Assert.IsFalse(prices.Any(p => p.Population == price.Population && p.PricePop == oldPrice));
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdatePricePopulationAlreadyExists()
        {
            var prices = ZooInfoController.GetAllPrices((int)Languages.en);
            Assert.AreEqual(5, prices.Count());

            var price = new Price
            {
                Id = 9,
                Population = "Adult",
                PricePop = 10,
                Language = (int)Languages.en
            };

            ZooInfoController.UpdatePrice(price);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdatePricePopulationIdDoesntExists()
        {
            var prices = ZooInfoController.GetAllPrices((int)Languages.en);
            Assert.AreEqual(5, prices.Count());

            var price = new Price
            {
                Id = -2,
                Population = "Student",
                PricePop = 10,
                Language = (int)Languages.en
            };

            ZooInfoController.UpdatePrice(price);
        }
        #endregion

        #region DeletePrice
        [TestMethod]
        public void DeletePriceValidInput()
        {
            var prices = ZooInfoController.GetAllPrices((int)Languages.he);
            Assert.AreEqual(5, prices.Count());

            var price = prices.SingleOrDefault(p => p.Population == "סטודנט");
            Assert.IsNotNull(price);

            ZooInfoController.DeletePrice(price.Id);

            prices = ZooInfoController.GetAllPrices((int)Languages.he);
            Assert.AreEqual(4, prices.Count());
            Assert.IsFalse(prices.Any(p => p.Population == "סטודנט"));

            prices = ZooInfoController.GetAllPrices((int)Languages.en);
            Assert.AreEqual(5, prices.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void DeletePriceIdDoesntExists()
        {
            ZooInfoController.DeletePrice(-4);
        }
        #endregion

        #endregion

        #region OpeningHour

        #region getAllOpeningHours
        [TestMethod]
        public void GetAllOpeningHours()
        {
            var openingHours = ZooInfoController.GetAllOpeningHours();
            Assert.IsInstanceOfType(openingHours, typeof(OpeningHour[]));

            Assert.AreEqual(6, openingHours.Count());
            Assert.IsTrue(openingHours.Any(oh => oh.Day == "ראשון"));
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetAllOpeningHoursLangDoeantExists()
        {
            ZooInfoController.GetAllOpeningHours(nonExistantLangauge);
        }

        #endregion

        #region updateOpeningHour
        [TestMethod]
        public void UpdateOpeningHourAddValidInput()
        {
            var openingHours = ZooInfoController.GetAllOpeningHours((int)Languages.en);
            Assert.IsInstanceOfType(openingHours, typeof(OpeningHour[]));

            Assert.AreEqual(6, openingHours.Count());
            Assert.IsTrue(openingHours.Any(oh => oh.Day == "Sunday"));

            OpeningHour opHour = new OpeningHour
            {
                Id = default(int),
                Day = "Friday",
                StartHour = 9,
                StartMin = (int)AvailableMinutes.ThreeQuarters,
                EndHour = 18,
                EndMin = (int)AvailableMinutes.Zero,
                Language = (int)Languages.en
            };

            ZooInfoController.UpdateOpeningHour(opHour);

            openingHours = ZooInfoController.GetAllOpeningHours();
            Assert.AreEqual(6, openingHours.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateOpeningHourAddDayAlreadyExists()
        {
            var openingHours = ZooInfoController.GetAllOpeningHours();
            Assert.IsInstanceOfType(openingHours, typeof(OpeningHour[]));

            Assert.AreEqual(6, openingHours.Count());
            Assert.IsTrue(openingHours.Any(oh => oh.Day == "ראשון"));

            OpeningHour opHour = new OpeningHour
            {
                Id = default(int),
                Day = "Saturday",
                StartHour = 9,
                StartMin = (int)AvailableMinutes.ThreeQuarters,
                EndHour = 18,
                EndMin = (int)AvailableMinutes.Zero,
                Language = (int)Languages.en
            };

            ZooInfoController.UpdateOpeningHour(opHour);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateOpeningHourDayWhiteSpaces()
        {
            var openingHours = ZooInfoController.GetAllOpeningHours();
            Assert.IsInstanceOfType(openingHours, typeof(OpeningHour[]));

            Assert.AreEqual(6, openingHours.Count());
            Assert.IsTrue(openingHours.Any(oh => oh.Day == "ראשון"));

            OpeningHour opHour = new OpeningHour
            {
                Id = default(int),
                Day = "    ",
                StartHour = 9,
                StartMin = (int)AvailableMinutes.ThreeQuarters,
                EndHour = 18,
                EndMin = (int)AvailableMinutes.Zero,
                Language = (int)Languages.en
            };

            ZooInfoController.UpdateOpeningHour(opHour);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateOpeningHourDayEmpty()
        {
            var openingHours = ZooInfoController.GetAllOpeningHours();
            Assert.IsInstanceOfType(openingHours, typeof(OpeningHour[]));

            Assert.AreEqual(6, openingHours.Count());
            Assert.IsTrue(openingHours.Any(oh => oh.Day == "ראשון"));

            OpeningHour opHour = new OpeningHour
            {
                Id = default(int),
                Day = "",
                StartHour = 9,
                StartMin = (int)AvailableMinutes.ThreeQuarters,
                EndHour = 18,
                EndMin = (int)AvailableMinutes.Zero,
                Language = (int)Languages.en
            };

            ZooInfoController.UpdateOpeningHour(opHour);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateOpeningHourWrongDate()
        {
            var openingHours = ZooInfoController.GetAllOpeningHours();
            Assert.IsInstanceOfType(openingHours, typeof(OpeningHour[]));

            Assert.AreEqual(6, openingHours.Count());
            Assert.IsTrue(openingHours.Any(oh => oh.Day == "ראשון"));

            OpeningHour opHour = new OpeningHour
            {
                Id = default(int),
                Day = "Kaki",
                EndHour = 9,
                EndMin = (int)AvailableMinutes.ThreeQuarters,
                StartHour = 18,
                StartMin = (int)AvailableMinutes.Zero,
                Language = (int)Languages.en
            };

            ZooInfoController.UpdateOpeningHour(opHour);
        }

        [TestMethod]
        public void UpdateOpeningHour()
        {
            var openingHours = ZooInfoController.GetAllOpeningHours();
            Assert.AreEqual(6, openingHours.Count());

            OpeningHour opHour = new OpeningHour
            {
                Id = 11,
                Day = "שבת",
                StartHour = 10,
                StartMin = (int)AvailableMinutes.ThreeQuarters,
                EndHour = 18,
                EndMin = (int)AvailableMinutes.Zero,
                Language = (int)Languages.he
            };

            opHour.EndHour = 20;

            ZooInfoController.UpdateOpeningHour(opHour);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateOpeningHourDayAlreadyExists()
        {
            var openingHours = ZooInfoController.GetAllOpeningHours();
            Assert.AreEqual(6, openingHours.Count());

            OpeningHour opHour = new OpeningHour
            {
                Id = 11,
                Day = "שלישי",
                StartHour = 10,
                StartMin = (int)AvailableMinutes.ThreeQuarters,
                EndHour = 18,
                EndMin = (int)AvailableMinutes.Zero,
                Language = (int)Languages.he
            };

            ZooInfoController.UpdateOpeningHour(opHour);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateOpeningHourIdDoesntExists()
        {
            var openingHours = ZooInfoController.GetAllOpeningHours();
            Assert.AreEqual(6, openingHours.Count());

            OpeningHour opHour = new OpeningHour
            {
                Id = -11,
                Day = "שבת",
                StartHour = 10,
                StartMin = (int)AvailableMinutes.ThreeQuarters,
                EndHour = 18,
                EndMin = (int)AvailableMinutes.Zero,
                Language = (int)Languages.he
            };

            ZooInfoController.UpdateOpeningHour(opHour);
        }

        #endregion

        #region DeleteOpeningHour
        [TestMethod]
        public void DeleteOpeningHourValidInput()
        {
            var openingHours = ZooInfoController.GetAllOpeningHours((int)Languages.en);
            Assert.AreEqual(6, openingHours.Count());

            OpeningHour opHour = openingHours.SingleOrDefault(oh => oh.Day == "Sunday");

            ZooInfoController.DeleteOpeningHour(opHour.Id);

            openingHours = ZooInfoController.GetAllOpeningHours();
            Assert.AreEqual(6, openingHours.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void DeleteOpeningHourIdDoesntExists()
        {
            ZooInfoController.DeleteOpeningHour(-4);
        }
        #endregion

        #endregion

        #region contactInfo

        #region getAllContactInfo
        [TestMethod]
        public void GetAllContactInfos()
        {
            var contactInfos = ZooInfoController.GetAllContactInfos();
            Assert.AreEqual(3, contactInfos.Count());
            Assert.IsTrue(contactInfos.Any(ci => ci.Via == "דואר"));
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetAllContactInfosLanguageNotExists()
        {
            ZooInfoController.GetAllContactInfos(nonExistantLangauge);
        }

        #endregion

        #region updateContactInfo
        [TestMethod]
        public void UpdateContactAddValidInput()
        {
            var contactInfos = ZooInfoController.GetAllContactInfos((int)Languages.en);
            Assert.AreEqual(3, contactInfos.Count());

            ContactInfo contact = new ContactInfo
            {
                Id = default(int),
                Via = "E-mail",
                Address = "avivmag@post.bgu.ac.il",
                Language = (int)Languages.en
            };

            ZooInfoController.UpdateContactInfo(contact);

            contactInfos = ZooInfoController.GetAllContactInfos((int)Languages.en);
            Assert.AreEqual(4, contactInfos.Count());

            contactInfos = ZooInfoController.GetAllContactInfos((int)Languages.he);
            Assert.AreEqual(3, contactInfos.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateContactAddAddresAndViaExists()
        {
            var contactInfos = ZooInfoController.GetAllContactInfos((int)Languages.en);
            Assert.AreEqual(3, contactInfos.Count());

            ContactInfo contact = new ContactInfo
            {
                Id = default(int),
                Via = "E-Mail",
                Address = "gilorisr@post.bgu.ac.il",
                Language = (int)Languages.en
            };

            ZooInfoController.UpdateContactInfo(contact);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateContactViaWhiteSpaces()
        {
            var contactInfos = ZooInfoController.GetAllContactInfos((int)Languages.en);
            Assert.AreEqual(3, contactInfos.Count());

            ContactInfo contact = new ContactInfo
            {
                Id = default(int),
                Via = "     ",
                Address = "avivmag@post.bgu.ac.il",
                Language = (int)Languages.en
            };

            ZooInfoController.UpdateContactInfo(contact);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateContactViaEmpty()
        {
            var contactInfos = ZooInfoController.GetAllContactInfos((int)Languages.en);
            Assert.AreEqual(3, contactInfos.Count());

            ContactInfo contact = new ContactInfo
            {
                Id = default(int),
                Via = "",
                Address = "avivmag@post.bgu.ac.il",
                Language = (int)Languages.en
            };

            ZooInfoController.UpdateContactInfo(contact);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateContactAddressWhiteSpaces()
        {
            var contactInfos = ZooInfoController.GetAllContactInfos((int)Languages.en);
            Assert.AreEqual(3, contactInfos.Count());

            ContactInfo contact = new ContactInfo
            {
                Id = default(int),
                Via = "E-mail",
                Address = "         ",
                Language = (int)Languages.en
            };

            ZooInfoController.UpdateContactInfo(contact);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateContactAddressEmpty()
        {
            var contactInfos = ZooInfoController.GetAllContactInfos((int)Languages.en);
            Assert.AreEqual(3, contactInfos.Count());

            ContactInfo contact = new ContactInfo
            {
                Id = default(int),
                Via = "E-mail",
                Address = "",
                Language = (int)Languages.en
            };

            ZooInfoController.UpdateContactInfo(contact);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateContactWrongLanguage()
        {
            var contactInfos = ZooInfoController.GetAllContactInfos((int)Languages.en);
            Assert.AreEqual(3, contactInfos.Count());

            ContactInfo contact = new ContactInfo
            {
                Id = default(int),
                Via = "E-mail",
                Address = "This is address",
                Language = nonExistantLangauge
            };

            ZooInfoController.UpdateContactInfo(contact);
        }

        [TestMethod]
        public void UpdateContactValidInput()
        {
            var contactInfos = ZooInfoController.GetAllContactInfos((int)Languages.he);
            Assert.AreEqual(3, contactInfos.Count());

            ContactInfo contact = new ContactInfo
            {
                Id = 3,
                Via = "דואר",
                Address = "דרך אילן רמון 5, באר שבע",
                Language = (int)Languages.he
            };

            var oldAddress = contact.Address;
            contact.Address = "יש לנו כתובת חדשה!";

            ZooInfoController.UpdateContactInfo(contact);

            contactInfos = ZooInfoController.GetAllContactInfos();
            Assert.AreEqual(3, contactInfos.Count());
            Assert.IsTrue(contactInfos.Any(ci => ci.Address == contact.Address));
            Assert.IsFalse(contactInfos.Any(ci => ci.Address == oldAddress));
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateContactViaAndAddressAlreadyExists()
        {
            var contactInfos = ZooInfoController.GetAllContactInfos((int)Languages.he);
            Assert.AreEqual(3, contactInfos.Count());

            ContactInfo contact = new ContactInfo
            {
                Id = 3,
                Via = "דואר",
                Address = "דרך אילן רמון 5, באר שבע",
                Language = (int)Languages.he
            };

            contact.Via = "טלפון";
            contact.Address = "08-641-4777";

            ZooInfoController.UpdateContactInfo(contact);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateContactIdDoesntExists()
        {
            var contactInfos = ZooInfoController.GetAllContactInfos((int)Languages.he);
            Assert.AreEqual(3, contactInfos.Count());

            ContactInfo contact = new ContactInfo
            {
                Id = 3,
                Via = "דואר",
                Address = "דרך אילן רמון 5, באר שבע",
                Language = (int)Languages.he
            };

            contact.Id = -3;

            ZooInfoController.UpdateContactInfo(contact);
        }
        #endregion

        #region deleteContactInfo
        [TestMethod]
        public void DeleteContactInfoValidInput()
        {
            var contactInfos = ZooInfoController.GetAllContactInfos((int)Languages.en);
            Assert.AreEqual(3, contactInfos.Count());

            ContactInfo contact = contactInfos.SingleOrDefault(ci => ci.Via == "Post Mail");

            ZooInfoController.DeleteContactInfo(contact.Id);

            Assert.AreEqual(3, contactInfos.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void DeleteContactInfoIdDoesntExists()
        {
            ZooInfoController.DeleteContactInfo(-4);
        }
        #endregion

        #endregion

        #region Special event

        #region getAllSpecialEvents
        [TestMethod]
        public void GetAllSpecialEvents()
        {
            var specialEvents = ZooInfoController.GetAllSpecialEvents();
            Assert.AreEqual(2, specialEvents.Count());
            Assert.IsTrue(specialEvents.Any(se => se.Description == "1קקי"));
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetAllSpecialEventsLanguageNotExists()
        {
            ZooInfoController.GetAllSpecialEvents(nonExistantLangauge);
        }
        #endregion

        #region getSpecialEventsByDates
        [TestMethod]
        public void GetSpecialEventsByDates()
        {
            var specialEvents = ZooInfoController.GetAllSpecialEventsByDates(new DateTime(2018,03,01),new DateTime(2018,03,31),(int)Languages.en);

            Assert.AreEqual(2, specialEvents.Count());
            Assert.IsTrue(specialEvents.Any(se => se.Description == "Kaki1"));

            specialEvents = ZooInfoController.GetAllSpecialEventsByDates(new DateTime(2018, 03, 02), new DateTime(2018, 03, 31), (int)Languages.en);
            Assert.AreEqual(1, specialEvents.Count());
            Assert.IsTrue(specialEvents.Any(se => se.Description == "Kaki1"));
            Assert.IsFalse(specialEvents.Any(se => se.Description == "Purim Events"));
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetAllSpecialEventsByDatesWrongDates()
        {
            ZooInfoController.GetAllSpecialEventsByDates(new DateTime(2018, 03, 31), new DateTime(2018, 03, 01), nonExistantLangauge);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetAllSpecialEventsByDatesLanguageNotExists()
        {
            ZooInfoController.GetAllSpecialEventsByDates(new DateTime(2018, 03, 01), new DateTime(2018, 03, 31), nonExistantLangauge);
        }
        #endregion

        #region updateSpecialEvent
        [TestMethod]
        public void UpdateSpecialEventsAddValidInput()
        {
            var specialEvents = ZooInfoController.GetAllSpecialEvents((int)Languages.en);
            Assert.AreEqual(2, specialEvents.Count());

            SpecialEvent eve = new SpecialEvent
            {
                Id = default(int),
                Description = "The big holiday",
                StartDate = new DateTime(2018, 7, 1),
                EndDate = new DateTime(2018,9,1),
                Language = (int)Languages.en
            };

            ZooInfoController.UpdateSpecialEvent(eve);

            specialEvents = ZooInfoController.GetAllSpecialEvents((int)Languages.en);
            Assert.AreEqual(3, specialEvents.Count());

            specialEvents = ZooInfoController.GetAllSpecialEvents((int)Languages.he);
            Assert.AreEqual(2, specialEvents.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateSpecialEventAddDescriptionExists()
        {
            var specialEvents = ZooInfoController.GetAllSpecialEvents((int)Languages.en);
            Assert.AreEqual(2, specialEvents.Count());

            SpecialEvent eve = new SpecialEvent
            {
                Id = default(int),
                Description = "Kaki1",
                StartDate = new DateTime(2018, 7, 1),
                EndDate = new DateTime(2018, 9, 1),
                Language = (int)Languages.en
            };

            ZooInfoController.UpdateSpecialEvent(eve);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateSpecailEventsInvalidDates()
        {
            var specialEvents = ZooInfoController.GetAllSpecialEvents((int)Languages.en);
            Assert.AreEqual(2, specialEvents.Count());

            SpecialEvent eve = new SpecialEvent
            {
                Id = default(int),
                Description = "The big holiday",
                EndDate = new DateTime(2018, 7, 1),
                StartDate = new DateTime(2018, 9, 1),
                Language = (int)Languages.en
            };

            ZooInfoController.UpdateSpecialEvent(eve);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateSpecialEventsWrongLanguage()
        {
            var specialEvents = ZooInfoController.GetAllSpecialEvents((int)Languages.en);
            Assert.AreEqual(2, specialEvents.Count());

            SpecialEvent eve = new SpecialEvent
            {
                Id = default(int),
                Description = "The big holiday",
                StartDate = new DateTime(2018, 7, 1),
                EndDate = new DateTime(2018, 9, 1),
                Language = nonExistantLangauge
            };

            ZooInfoController.UpdateSpecialEvent(eve);
        }

        [TestMethod]
        public void UpdateSpecialEventsValidInput()
        {
            var specialEvents = ZooInfoController.GetAllSpecialEvents((int)Languages.en);
            Assert.AreEqual(2, specialEvents.Count());

            SpecialEvent eve = new SpecialEvent
            {
                Id = 2,
                Description = "Kaki1",
                StartDate = new DateTime(2018, 3, 5),
                EndDate = new DateTime(2018, 3, 8),
                Language = (int)Languages.en
            };

            var oldDescription = eve.Description;
            eve.Description = "New Event";

            ZooInfoController.UpdateSpecialEvent(eve);

            specialEvents = ZooInfoController.GetAllSpecialEvents((int)Languages.en);
            Assert.AreEqual(2, specialEvents.Count());
            Assert.IsTrue(specialEvents.Any(se => se.Description == eve.Description));
            Assert.IsFalse(specialEvents.Any(se => se.Description == oldDescription));
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateSpecialEventsDescriptionAlreadyExists()
        {
            var specialEvents = ZooInfoController.GetAllSpecialEvents((int)Languages.en);
            Assert.AreEqual(2, specialEvents.Count());

            SpecialEvent eve = new SpecialEvent
            {
                Id = 2,
                Description = "Kaki1",
                StartDate = new DateTime(2018, 3, 5),
                EndDate = new DateTime(2018, 3, 8),
                Language = (int)Languages.en
            };

            eve.Description = "Purim Events";

            ZooInfoController.UpdateSpecialEvent(eve);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateSpecialEventsIdDoesntExists()
        {
            var specialEvents = ZooInfoController.GetAllSpecialEvents((int)Languages.he);
            Assert.AreEqual(2, specialEvents.Count());

            SpecialEvent eve = new SpecialEvent
            {
                Id = 2,
                Description = "Kaki1",
                StartDate = new DateTime(2018, 3, 5),
                EndDate = new DateTime(2018, 3, 8),
                Language = (int)Languages.en
            };

            eve.Id = -3;

            ZooInfoController.UpdateSpecialEvent(eve);
        }
        #endregion

        #region deleteSpecialEvent
        [TestMethod]
        public void DeleteSpecialEventValidInput()
        {
            var specialEvents = ZooInfoController.GetAllSpecialEvents();
            Assert.AreEqual(2, specialEvents.Count());

            SpecialEvent eve = specialEvents.SingleOrDefault(se => se.Description == "אירועי פורים");

            ZooInfoController.DeleteSpecialEvent(eve.Id);

            specialEvents = ZooInfoController.GetAllSpecialEvents();
            Assert.AreEqual(1, specialEvents.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void DeleteSpecialEventsIdDoesntExists()
        {
            ZooInfoController.DeleteSpecialEvent(-4);
        }
        #endregion

        #endregion

        #region GeneralInforamtion

        #region getAboutUs
        [TestMethod]
        public void GetAboutUs()
        {
            var aboutUs = ZooInfoController.GetZooAboutInfo(1);
            Assert.IsInstanceOfType(aboutUs, typeof(ZooInfoController.AboutUsResult[]));

            Assert.AreEqual(aboutUs.Count(), 1);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetAboutUsLangaugeNotExist()
        {
            ZooInfoController.GetZooAboutInfo(nonExistantLangauge);
        }
        #endregion

        #region updateAboutUs
        [TestMethod]
        public void UpdateAboutUsValidInput()
        {
            var aboutUs = ZooInfoController.GetZooAboutInfo((int)Languages.en);
            Assert.AreEqual(aboutUs.Count(), 1);

            var oldAboutUs = aboutUs.SingleOrDefault(au => au.aboutUs == "We are Negev Zoo!!! We love animals");
            var newAboutUs = "This is the new about us!";

            ZooInfoController.UpdateZooAboutInfo(newAboutUs, (int)Languages.en);

            aboutUs = ZooInfoController.GetZooAboutInfo((int)Languages.en);
            Assert.IsTrue(aboutUs.Any(au => au.aboutUs == newAboutUs));
            Assert.IsFalse(aboutUs.Any(au => au.aboutUs == oldAboutUs.aboutUs));
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateAboutUsWrongAboutUsWhiteSpaces()
        {
            ZooInfoController.UpdateZooAboutInfo("      ", (int)Languages.en);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateAboutUsWrongAboutUsEmpty()
        {
            ZooInfoController.UpdateZooAboutInfo("", (int)Languages.en);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateAboutUsLangaugeNotExist()
        {
            ZooInfoController.UpdateZooAboutInfo("This is the new about us !", nonExistantLangauge);
        }
        #endregion

        #region getOpeningHourNote
        [TestMethod]
        public void GetOpeningHourNote()
        {
            var ohNote= ZooInfoController.GetOpeningHourNote((int)Languages.en);
            Assert.AreEqual(ohNote.Count(), 1);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetOpeningHourNoteLangaugeNotExist()
        {
            ZooInfoController.GetOpeningHourNote(nonExistantLangauge);
        }

        #endregion

        #region updateOpeningHourNote
        [TestMethod]
        public void UpdateOpeningHourNoteValidInput()
        {
            var ohNote = ZooInfoController.GetOpeningHourNote((int)Languages.en);
            Assert.AreEqual(ohNote.Count(), 1);

            var oldNote = ohNote.SingleOrDefault(ohn => ohn.aboutUs == "The cashier desk will bew closed two hours before the zoo is closing.");
            var newAboutUs = "This is the new note!";

            ZooInfoController.UpdateOpeningHourNote(newAboutUs, (int)Languages.en);

            ohNote = ZooInfoController.GetOpeningHourNote((int)Languages.en);
            Assert.IsTrue(ohNote.Any(ohn => ohn.aboutUs == newAboutUs));
            Assert.IsFalse(ohNote.Any(ohn => ohn.aboutUs == oldNote.aboutUs));
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateOpeningHourNoteWrongAboutUsWhiteSpaces()
        {
            ZooInfoController.UpdateOpeningHourNote("      ", (int)Languages.en);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateOpeningHourNoteWrongAboutUsEmpty()
        {
            ZooInfoController.UpdateOpeningHourNote("", (int)Languages.en);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateOpeningHourNoteLangaugeNotExist()
        {
            ZooInfoController.UpdateOpeningHourNote("This is the new note!", nonExistantLangauge);
        }

        #endregion

        #region getContactInfoNote
        [TestMethod]
        public void GetContacgInfoNote()
        {
            var ciNote = ZooInfoController.GetContactInfoNote((int)Languages.en);
            Assert.AreEqual(ciNote.Count(), 1);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetContactInfoNoteLangaugeNotExist()
        {
            ZooInfoController.GetContactInfoNote(nonExistantLangauge);
        }

        #endregion

        #region updateOpeningHourNote
        [TestMethod]
        public void UpdateContactInfoNoteValidInput()
        {
            var ciNote = ZooInfoController.GetContactInfoNote((int)Languages.en);
            Assert.AreEqual(ciNote.Count(), 1);

            var oldNote = ciNote.SingleOrDefault(cin => cin.aboutUs == "Contact between 08:00 - 22:00");
            var newAboutUs = "This is the new note!";

            ZooInfoController.UpdateContactInfoNote(newAboutUs, (int)Languages.en);

            ciNote = ZooInfoController.GetContactInfoNote((int)Languages.en);
            Assert.IsTrue(ciNote.Any(cin => cin.aboutUs == newAboutUs));
            Assert.IsFalse(ciNote.Any(cin => cin.aboutUs == oldNote.aboutUs));
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateContactInfoNoteWrongAboutUsWhiteSpaces()
        {
            ZooInfoController.UpdateContactInfoNote("      ", (int)Languages.en);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateContactInfoNoteWrongAboutUsEmpty()
        {
            ZooInfoController.UpdateContactInfoNote("", (int)Languages.en);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateContactInfoNoteLangaugeNotExist()
        {
            ZooInfoController.UpdateContactInfoNote("This is the new note!", nonExistantLangauge);
        }

        #endregion

        #endregion
    }
}
