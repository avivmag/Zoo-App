﻿using DAL;
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
                id = default(int),
                created = new DateTime(2018, 03, 14),
                info = "Another Kaki appears in the zoo",
                language =(int)Languages.en
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
                id = default(int),
                created = new DateTime(2018, 03, 05),
                info = "Purim Events",
                language = (int)Languages.en
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
                id = default(int),
                created = new DateTime(2018, 03, 05),
                info = "Purim Events",
                language = nonExistantLangauge
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
                id = default(int),
                created = new DateTime(2019, 03, 05),
                info = "Purim Events",
                language = (int)Languages.en
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
                id = default(int),
                created = new DateTime(2018, 03, 05),
                info = "   ",
                language = (int)Languages.en
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
                id = default(int),
                created = new DateTime(2018, 03, 05),
                info = "",
                language = (int)Languages.en
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
                id = 1,
                created = new DateTime(2018, 03, 05),
                info = "Purim is back again!",
                language = (int)Languages.en
            };

            ZooInfoController.UpdateWallFeed(feed);

            feeds = ZooInfoController.GetAllFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());
            Assert.IsTrue(feeds.Any(f => f.info == feed.info));
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateWallFeedInfoAlreadyExists()
        {
            var feeds = ZooInfoController.GetAllFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());

            var feed = new WallFeed
            {
                id = 1,
                created = new DateTime(2018, 03, 05),
                info = "Sukut Events",
                language = (int)Languages.en
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
                id = -4,
                created = new DateTime(2018, 03, 05),
                info = "Purim is back again!",
                language = (int)Languages.en
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

            var firstFeed = feeds.SingleOrDefault(f => f.id == 1);
            Assert.IsNotNull(firstFeed);

            ZooInfoController.DeleteWallFeed((int)firstFeed.id);

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
            Assert.IsTrue(prices.Any(p => p.population == "מבוגר"));
        }

        [TestMethod]
        public void GetAllPricesLangEng()
        {
            var prices = ZooInfoController.GetAllPrices(2);
            Assert.IsInstanceOfType(prices, typeof(Price[]));

            Assert.AreEqual(5, prices.Count());
            Assert.IsTrue(prices.Any(p => p.population == "Adult"));
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
                id = default(int),
                population = "Veteran",
                pricePop = 15.5,
                language = (int)Languages.en
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
                id = default(int),
                population = "Adult",
                pricePop = 15.5,
                language = (int)Languages.en
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
                id = default(int),
                population = "Veteran",
                pricePop = 15.5,
                language = nonExistantLangauge
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
                id = default(int),
                population = "   ",
                pricePop = 15.5,
                language = (int)Languages.en
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
                id = default(int),
                population = "",
                pricePop = 15.5,
                language = (int)Languages.en
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
                id = default(int),
                population = "Veteran",
                pricePop = -15.5,
                language = (int)Languages.en
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
                id = 9,
                population = "Student",
                pricePop = 10,
                language = (int)Languages.en
            };
            var oldPrice = price.pricePop;
            price.pricePop = 20;

            ZooInfoController.UpdatePrice(price);

            prices = ZooInfoController.GetAllPrices((int)Languages.en);
            Assert.AreEqual(5, prices.Count());
            Assert.IsTrue(prices.Any(p => p.population == price.population && p.pricePop == price.pricePop));
            Assert.IsFalse(prices.Any(p => p.population == price.population && p.pricePop == oldPrice));
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdatePricePopulationAlreadyExists()
        {
            var prices = ZooInfoController.GetAllPrices((int)Languages.en);
            Assert.AreEqual(5, prices.Count());

            var price = new Price
            {
                id = 9,
                population = "Adult",
                pricePop = 10,
                language = (int)Languages.en
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
                id = -2,
                population = "Student",
                pricePop = 10,
                language = (int)Languages.en
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

            var price = prices.SingleOrDefault(p => p.population == "סטודנט");
            Assert.IsNotNull(price);

            ZooInfoController.DeletePrice(price.id);

            prices = ZooInfoController.GetAllPrices((int)Languages.he);
            Assert.AreEqual(4, prices.Count());
            Assert.IsFalse(prices.Any(p => p.population == "סטודנט"));

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
            Assert.IsTrue(openingHours.Any(oh => oh.day == "ראשון"));
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
            Assert.IsTrue(openingHours.Any(oh => oh.day == "Sunday"));

            OpeningHour opHour = new OpeningHour
            {
                id = default(int),
                day = "Friday",
                startHour = 9,
                startMin = (int)AvailableMinutes.ThreeQuarters,
                endHour = 18,
                endMin = (int)AvailableMinutes.Zero,
                language = (int)Languages.en
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
            Assert.IsTrue(openingHours.Any(oh => oh.day == "ראשון"));

            OpeningHour opHour = new OpeningHour
            {
                id = default(int),
                day = "Saturday",
                startHour = 9,
                startMin = (int)AvailableMinutes.ThreeQuarters,
                endHour = 18,
                endMin = (int)AvailableMinutes.Zero,
                language = (int)Languages.en
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
            Assert.IsTrue(openingHours.Any(oh => oh.day == "ראשון"));

            OpeningHour opHour = new OpeningHour
            {
                id = default(int),
                day = "    ",
                startHour = 9,
                startMin = (int)AvailableMinutes.ThreeQuarters,
                endHour = 18,
                endMin = (int)AvailableMinutes.Zero,
                language = (int)Languages.en
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
            Assert.IsTrue(openingHours.Any(oh => oh.day == "ראשון"));

            OpeningHour opHour = new OpeningHour
            {
                id = default(int),
                day = "",
                startHour = 9,
                startMin = (int)AvailableMinutes.ThreeQuarters,
                endHour = 18,
                endMin = (int)AvailableMinutes.Zero,
                language = (int)Languages.en
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
            Assert.IsTrue(openingHours.Any(oh => oh.day == "ראשון"));

            OpeningHour opHour = new OpeningHour
            {
                id = default(int),
                day = "Kaki",
                endHour = 9,
                endMin = (int)AvailableMinutes.ThreeQuarters,
                startHour = 18,
                startMin = (int)AvailableMinutes.Zero,
                language = (int)Languages.en
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
                id = 11,
                day = "שבת",
                startHour = 10,
                startMin = (int)AvailableMinutes.ThreeQuarters,
                endHour = 18,
                endMin = (int)AvailableMinutes.Zero,
                language = (int)Languages.he
            };

            opHour.endHour = 20;

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
                id = 11,
                day = "שלישי",
                startHour = 10,
                startMin = (int)AvailableMinutes.ThreeQuarters,
                endHour = 18,
                endMin = (int)AvailableMinutes.Zero,
                language = (int)Languages.he
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
                id = -11,
                day = "שבת",
                startHour = 10,
                startMin = (int)AvailableMinutes.ThreeQuarters,
                endHour = 18,
                endMin = (int)AvailableMinutes.Zero,
                language = (int)Languages.he
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

            OpeningHour opHour = openingHours.SingleOrDefault(oh => oh.day == "Sunday");

            ZooInfoController.DeleteOpeningHour(opHour.id);

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
            Assert.IsTrue(contactInfos.Any(ci => ci.via == "דואר"));
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
                id = default(int),
                via = "E-mail",
                address = "avivmag@post.bgu.ac.il",
                language = (int)Languages.en
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
                id = default(int),
                via = "E-Mail",
                address = "gilorisr@post.bgu.ac.il",
                language = (int)Languages.en
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
                id = default(int),
                via = "     ",
                address = "avivmag@post.bgu.ac.il",
                language = (int)Languages.en
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
                id = default(int),
                via = "",
                address = "avivmag@post.bgu.ac.il",
                language = (int)Languages.en
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
                id = default(int),
                via = "E-mail",
                address = "         ",
                language = (int)Languages.en
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
                id = default(int),
                via = "E-mail",
                address = "",
                language = (int)Languages.en
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
                id = default(int),
                via = "E-mail",
                address = "This is address",
                language = nonExistantLangauge
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
                id = 3,
                via = "דואר",
                address = "דרך אילן רמון 5, באר שבע",
                language = (int)Languages.he
            };

            var oldAddress = contact.address;
            contact.address = "יש לנו כתובת חדשה!";

            ZooInfoController.UpdateContactInfo(contact);

            contactInfos = ZooInfoController.GetAllContactInfos();
            Assert.AreEqual(3, contactInfos.Count());
            Assert.IsTrue(contactInfos.Any(ci => ci.address == contact.address));
            Assert.IsFalse(contactInfos.Any(ci => ci.address == oldAddress));
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateContactViaAndAddressAlreadyExists()
        {
            var contactInfos = ZooInfoController.GetAllContactInfos((int)Languages.he);
            Assert.AreEqual(3, contactInfos.Count());

            ContactInfo contact = new ContactInfo
            {
                id = 3,
                via = "דואר",
                address = "דרך אילן רמון 5, באר שבע",
                language = (int)Languages.he
            };

            contact.via = "טלפון";
            contact.address = "08-641-4777";

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
                id = 3,
                via = "דואר",
                address = "דרך אילן רמון 5, באר שבע",
                language = (int)Languages.he
            };

            contact.id = -3;

            ZooInfoController.UpdateContactInfo(contact);
        }
        #endregion

        #region deleteContactInfo
        [TestMethod]
        public void DeleteContactInfoValidInput()
        {
            var contactInfos = ZooInfoController.GetAllContactInfos((int)Languages.en);
            Assert.AreEqual(3, contactInfos.Count());

            ContactInfo contact = contactInfos.SingleOrDefault(ci => ci.via == "Post Mail");

            ZooInfoController.DeleteContactInfo(contact.id);

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
            Assert.IsTrue(specialEvents.Any(se => se.description == "1קקי"));
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
            Assert.IsTrue(specialEvents.Any(se => se.description == "Kaki1"));

            specialEvents = ZooInfoController.GetAllSpecialEventsByDates(new DateTime(2018, 03, 02), new DateTime(2018, 03, 31), (int)Languages.en);
            Assert.AreEqual(1, specialEvents.Count());
            Assert.IsTrue(specialEvents.Any(se => se.description == "Kaki1"));
            Assert.IsFalse(specialEvents.Any(se => se.description == "Purim Events"));
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
                id = default(int),
                description = "The big holiday",
                startDate = new DateTime(2018, 7, 1),
                endDate = new DateTime(2018,9,1),
                language = (int)Languages.en
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
                id = default(int),
                description = "Kaki1",
                startDate = new DateTime(2018, 7, 1),
                endDate = new DateTime(2018, 9, 1),
                language = (int)Languages.en
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
                id = default(int),
                description = "The big holiday",
                endDate = new DateTime(2018, 7, 1),
                startDate = new DateTime(2018, 9, 1),
                language = (int)Languages.en
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
                id = default(int),
                description = "The big holiday",
                startDate = new DateTime(2018, 7, 1),
                endDate = new DateTime(2018, 9, 1),
                language = nonExistantLangauge
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
                id = 2,
                description = "Kaki1",
                startDate = new DateTime(2018, 3, 5),
                endDate = new DateTime(2018, 3, 8),
                language = (int)Languages.en
            };

            var oldDescription = eve.description;
            eve.description = "New Event";

            ZooInfoController.UpdateSpecialEvent(eve);

            specialEvents = ZooInfoController.GetAllSpecialEvents((int)Languages.en);
            Assert.AreEqual(2, specialEvents.Count());
            Assert.IsTrue(specialEvents.Any(se => se.description == eve.description));
            Assert.IsFalse(specialEvents.Any(se => se.description == oldDescription));
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateSpecialEventsDescriptionAlreadyExists()
        {
            var specialEvents = ZooInfoController.GetAllSpecialEvents((int)Languages.en);
            Assert.AreEqual(2, specialEvents.Count());

            SpecialEvent eve = new SpecialEvent
            {
                id = 2,
                description = "Kaki1",
                startDate = new DateTime(2018, 3, 5),
                endDate = new DateTime(2018, 3, 8),
                language = (int)Languages.en
            };

            eve.description = "Purim Events";

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
                id = 2,
                description = "Kaki1",
                startDate = new DateTime(2018, 3, 5),
                endDate = new DateTime(2018, 3, 8),
                language = (int)Languages.en
            };

            eve.id = -3;

            ZooInfoController.UpdateSpecialEvent(eve);
        }
        #endregion

        #region deleteSpecialEvent
        [TestMethod]
        public void DeleteSpecialEventValidInput()
        {
            var specialEvents = ZooInfoController.GetAllSpecialEvents();
            Assert.AreEqual(2, specialEvents.Count());

            SpecialEvent eve = specialEvents.SingleOrDefault(se => se.description == "אירועי פורים");

            ZooInfoController.DeleteSpecialEvent(eve.id);

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
