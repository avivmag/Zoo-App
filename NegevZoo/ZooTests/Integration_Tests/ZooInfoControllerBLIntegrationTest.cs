using DAL;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using NegevZoo.Controllers;
using System;
using System.Linq;
using System.Net.Http;
using System.Web.Http;

namespace ZooTests
{
    [TestClass]
    public class ZooInfoControllerBlIntegrationTest
    {
        #region SetUp and TearDown

        private ZooInfoController ZooInfoController;
        private int nonExistantLangauge;

        [TestInitialize]
        public void SetUp()
        {
            // The line below must be in every setup of each test. otherwise it will not be in a testing environment.
            ControllerBase.isTesting = true;
            ZooInfoController = new ZooInfoController();
            ZooInfoController.Request = new HttpRequestMessage();
            ZooInfoController.Request.Headers.Add("Cookie", "session-id=123");
            ZooInfoController.Request.RequestUri = new Uri("http://localhost:50000");
            nonExistantLangauge = 100;
        }

        [TestCleanup]
        public void EnclosureCleanUp()
        {
            DummyDB.CleanDb();
        }
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
                population = "Student",
                pricePop = 10,
                language = (int)Languages.en
            };

            price.population = "Adult";

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

        #region GetAllOpeningHourResults

        [TestMethod]
        public void GetAllOpeningHourResults()
        {
            var openingHours = ZooInfoController.GetAllOpeningHourResults();

            Assert.AreEqual(6, openingHours.Count());
            Assert.IsTrue(openingHours.Any(oh => oh.Day == "ראשון"));
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void GetAllOpeningHourReusltsLangDoeantExists()
        {
            ZooInfoController.GetAllOpeningHourResults(nonExistantLangauge);
        }

        #endregion

        #region GetAllOpeningHourResults

        [TestMethod]
        public void GetAllOpeningHourTypes()
        {
            var openingHours = ZooInfoController.GetAllOpeningHoursType();

            Assert.AreEqual(6, openingHours.Count());
            Assert.IsTrue(openingHours.Any(oh => oh.day == 1));
        }
        
        #endregion

        #region UpdateOpeningHour
        [TestMethod]
        public void UpdateOpeningHourAddValidInput()
        {
            var openingHours = ZooInfoController.GetAllOpeningHoursType();

            Assert.AreEqual(6, openingHours.Count());
            Assert.IsTrue(openingHours.Any(oh => oh.day == 1));
            Assert.IsFalse(openingHours.Any(oh => oh.day == 6));

            var openingHourResultHeb = ZooInfoController.GetAllOpeningHourResults();
            Assert.IsFalse(openingHourResultHeb.Any(oh => oh.Day == "שישי"));

            var openingHourResultEn = ZooInfoController.GetAllOpeningHourResults(2);
            Assert.IsFalse(openingHourResultHeb.Any(oh => oh.Day == "Friday"));

            OpeningHour opHour = new OpeningHour
            {
                id = default(int),
                day = 6,
                startTime = new TimeSpan(9, 45, 0),
                endTime = new TimeSpan(18, 0, 0)
            };

            ZooInfoController.UpdateOpeningHour(opHour);

            openingHours = ZooInfoController.GetAllOpeningHoursType();

            Assert.AreEqual(7, openingHours.Count());
            Assert.IsTrue(openingHours.Any(oh => oh.day == 6));

            //heb
            openingHourResultHeb = ZooInfoController.GetAllOpeningHourResults();
            Assert.AreEqual(7, openingHourResultHeb.Count());
            Assert.IsTrue(openingHourResultHeb.Any(oh => oh.Day == "שישי"));

            openingHourResultEn = ZooInfoController.GetAllOpeningHourResults(2);
            Assert.AreEqual(7, openingHourResultEn.Count());
            Assert.IsTrue(openingHourResultEn.Any(oh => oh.Day == "Friday"));
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateOpeningHourAddDayAlreadyExists()
        {
            var openingHours = ZooInfoController.GetAllOpeningHourResults();

            Assert.AreEqual(6, openingHours.Count());
            Assert.IsTrue(openingHours.Any(oh => oh.Day == "ראשון"));

            OpeningHour opHour = new OpeningHour
            {
                id = default(int),
                day = 7,
                startTime = new TimeSpan(9, 45, 0),
                endTime = new TimeSpan(18, 0, 0),
            };

            ZooInfoController.UpdateOpeningHour(opHour);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateOpeningHourWrongTime()
        {
            var openingHours = ZooInfoController.GetAllOpeningHourResults();

            Assert.AreEqual(6, openingHours.Count());
            Assert.IsTrue(openingHours.Any(oh => oh.Day == "ראשון"));

            OpeningHour opHour = new OpeningHour
            {
                id = default(int),
                day = 5,
                startTime = new TimeSpan(18, 0, 0),
                endTime = new TimeSpan(9, 45, 0),
            };

            ZooInfoController.UpdateOpeningHour(opHour);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateOpeningHourWrongDay()
        {
            var openingHours = ZooInfoController.GetAllOpeningHourResults();

            Assert.AreEqual(6, openingHours.Count());
            Assert.IsTrue(openingHours.Any(oh => oh.Day == "ראשון"));

            OpeningHour opHour = new OpeningHour
            {
                id = default(int),
                day = -1,
                startTime = new TimeSpan(9, 45, 0),
                endTime = new TimeSpan(18, 0, 0),
                language = (int)Languages.he
            };

            ZooInfoController.UpdateOpeningHour(opHour);
        }

        [TestMethod]
        public void UpdateOpeningHour()
        {
            var openingHours = ZooInfoController.GetAllOpeningHoursType();
            Assert.AreEqual(6, openingHours.Count());
            Assert.IsTrue(openingHours.Any(oh => oh.day == 1));

            var openingHourResultHeb = ZooInfoController.GetAllOpeningHourResults();
            Assert.IsTrue(openingHourResultHeb.Any(oh => oh.Day == "שבת"));

            var openingHourResultEn = ZooInfoController.GetAllOpeningHourResults(2);
            Assert.IsTrue(openingHourResultEn.Any(oh => oh.Day == "Saturday"));

            OpeningHour opHour = new OpeningHour
            {
                id = 11,
                day = 7,
                startTime = new TimeSpan(10, 45, 0),
                endTime = new TimeSpan(18, 0, 0),
                language = (int)Languages.he
            };

            var newTime = new TimeSpan(20, 0, 0);
            opHour.endTime = newTime;

            ZooInfoController.UpdateOpeningHour(opHour);

            openingHours = ZooInfoController.GetAllOpeningHoursType();

            opHour = openingHours.SingleOrDefault(oh => oh.day == 7);
            Assert.IsNotNull(opHour);
            Assert.AreEqual(newTime, opHour.endTime);

            //heb
            openingHourResultHeb = ZooInfoController.GetAllOpeningHourResults();
            Assert.AreEqual(6, openingHourResultHeb.Count());
            var fridayHeb = openingHourResultHeb.SingleOrDefault(oh => oh.Day == "שבת");
            Assert.IsNotNull(fridayHeb);
            Assert.AreEqual(newTime, fridayHeb.EndTime);
            
            openingHourResultEn = ZooInfoController.GetAllOpeningHourResults(2);
            Assert.AreEqual(6, openingHourResultEn.Count());
            var fridayEn = (openingHourResultEn.SingleOrDefault(oh => oh.Day == "Saturday"));
            Assert.IsNotNull(fridayEn);
            Assert.AreEqual(newTime, fridayEn.EndTime);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateOpeningHourDayAlreadyExists()
        {
            var openingHours = ZooInfoController.GetAllOpeningHourResults();
            Assert.AreEqual(6, openingHours.Count());

            OpeningHour opHour = new OpeningHour
            {
                id = 1,
                day = 2,
                startTime = new TimeSpan(11, 30, 00),
                endTime = new TimeSpan(12, 0, 0),
            };

            ZooInfoController.UpdateOpeningHour(opHour);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateOpeningHourIdDoesntExists()
        {
            var openingHours = ZooInfoController.GetAllOpeningHourResults();
            Assert.AreEqual(6, openingHours.Count());

            OpeningHour opHour = new OpeningHour
            {
                id = -11,
                day = 7,
                startTime = new TimeSpan(10, 45, 0),
                endTime = new TimeSpan(18, 0, 0),
            };

            ZooInfoController.UpdateOpeningHour(opHour);
        }

        #endregion

        #region DeleteOpeningHour
        [TestMethod]
        public void DeleteOpeningHourValidInput()
        {
            var openingHours = ZooInfoController.GetAllOpeningHoursType();
            Assert.AreEqual(6, openingHours.Count());

            //hebrew
            var openingHourResultsHeb = ZooInfoController.GetAllOpeningHourResults(1);
            Assert.AreEqual(6, openingHourResultsHeb.Count());

            //english
            var openingHourResultsEn = ZooInfoController.GetAllOpeningHourResults(2);
            Assert.AreEqual(6, openingHourResultsEn.Count());

            OpeningHour opHour = openingHours.SingleOrDefault(oh => oh.day == 1);

            ZooInfoController.DeleteOpeningHour(opHour.id);

            openingHours = ZooInfoController.GetAllOpeningHoursType();
            Assert.AreEqual(5, openingHours.Count());

            //hebrew
            openingHourResultsHeb = ZooInfoController.GetAllOpeningHourResults(1);
            Assert.AreEqual(5, openingHourResultsHeb.Count());
            Assert.IsFalse(openingHourResultsHeb.Any(oh => oh.Day == "ראשון"));

            //english
            openingHourResultsEn = ZooInfoController.GetAllOpeningHourResults(2);
            Assert.AreEqual(5, openingHourResultsEn.Count());
            Assert.IsFalse(openingHourResultsEn.Any(oh => oh.Day == "Sunday"));
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void DeleteOpeningHourIdDoesntExists()
        {
            ZooInfoController.DeleteOpeningHour(-4);
        }
        #endregion

        #endregion

        #region ContactInfo

        #region GetAllContactInfo
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
        
        #region UpdateContactInfo
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
        
        #region DeleteContactInfo
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

        #region GetAllSpecialEvents

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
        
        #region UpdateSpecialEvent

        [TestMethod]
        public void UpdateSpecialEventsAddValidInput()
        {
            var specialEvents = ZooInfoController.GetAllSpecialEvents((int)Languages.en);
            Assert.AreEqual(2, specialEvents.Count());

            SpecialEvent eve = new SpecialEvent
            {
                id = default(int),
                title = "The big holiday",
                description = "We have a Kaytana",
                startDate = new DateTime(2018, 7, 1),
                endDate = new DateTime(2018, 9, 1),
                language = (int)Languages.en
            };

            ZooInfoController.UpdateSpecialEvent(eve, false);

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
                title = "title1",
                description = "Kaki1",
                startDate = new DateTime(2018, 7, 1),
                endDate = new DateTime(2018, 9, 1),
                language = (int)Languages.en
            };

            ZooInfoController.UpdateSpecialEvent(eve, false);
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
                title = "The big holiday",
                description = "We have a Kaytana",
                endDate = new DateTime(2018, 7, 1),
                startDate = new DateTime(2018, 9, 1),
                language = (int)Languages.en
            };

            ZooInfoController.UpdateSpecialEvent(eve, false);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateSpecailEventsEmptyTitle()
        {
            var specialEvents = ZooInfoController.GetAllSpecialEvents((int)Languages.en);
            Assert.AreEqual(2, specialEvents.Count());

            SpecialEvent eve = new SpecialEvent
            {
                id = default(int),
                title = "",
                description = "We have a Kaytana",
                startDate= new DateTime(2018, 7, 1),
                endDate = new DateTime(2018, 9, 1),
                language = (int)Languages.en
            };

            ZooInfoController.UpdateSpecialEvent(eve, false);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateSpecailEventsWhiteSpacesDescription()
        {
            var specialEvents = ZooInfoController.GetAllSpecialEvents((int)Languages.en);
            Assert.AreEqual(2, specialEvents.Count());

            SpecialEvent eve = new SpecialEvent
            {
                id = default(int),
                title = "title",
                description = "     ",
                startDate = new DateTime(2018, 7, 1),
                endDate = new DateTime(2018, 9, 1),
                language = (int)Languages.en
            };

            ZooInfoController.UpdateSpecialEvent(eve, false);
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
                title = "The big holiday",
                description = "We have a Kaytana",
                startDate = new DateTime(2018, 7, 1),
                endDate = new DateTime(2018, 9, 1),
                language = nonExistantLangauge
            };

            ZooInfoController.UpdateSpecialEvent(eve, false);
        }

        [TestMethod]
        public void UpdateSpecialEventsValidInput()
        {
            var specialEvents = ZooInfoController.GetAllSpecialEvents((int)Languages.en);
            Assert.AreEqual(2, specialEvents.Count());

            SpecialEvent eve = new SpecialEvent
            {
                id = 2,
                title = "title1",
                description = "Kaki1",
                startDate = new DateTime(2018, 3, 5),
                endDate = new DateTime(2018, 3, 8),
                language = (int)Languages.en
            };

            var oldDescription = eve.description;
            eve.description = "New Event";

            ZooInfoController.UpdateSpecialEvent(eve, false);

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
                title = "title1",
                description = "Kaki1",
                startDate = new DateTime(2018, 3, 5),
                endDate = new DateTime(2018, 3, 8),
                language = (int)Languages.en
            };

            eve.title = "Purim Events";
            eve.description = "There are many kinds of events";

            ZooInfoController.UpdateSpecialEvent(eve, false);
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
                title = "title1",
                description = "Kaki1",
                startDate = new DateTime(2018, 3, 5),
                endDate = new DateTime(2018, 3, 8),
                language = (int)Languages.en
            };

            eve.id = -3;

            ZooInfoController.UpdateSpecialEvent(eve, false);
        }
        #endregion

        #region DeleteSpecialEvent

        [TestMethod]
        public void DeleteSpecialEventValidInput()
        {
            var specialEvents = ZooInfoController.GetAllSpecialEvents();
            Assert.AreEqual(2, specialEvents.Count());

            SpecialEvent eve = specialEvents.SingleOrDefault(se => se.title == "אירועי פורים");

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
        
        #region feeds

        #region GetAllWallFeed
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

        #region UpdateFeedWall
        [TestMethod]
        public void UpdateWallFeedAddFeedValidTest()
        {
            var feeds = ZooInfoController.GetAllFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());

            var feed = new WallFeed
            {
                id = default(int),
                title = "KAKI!!",
                info = "Another Kaki appears in the zoo",
                language =(int)Languages.en
            };

            ZooInfoController.UpdateWallFeed(feed, false, true);

            feeds = ZooInfoController.GetAllFeeds((int)Languages.en);
            Assert.AreEqual(17, feeds.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateWallFeedAddFeedInfoAndTitleAlreadyExists()
        {
            var feeds = ZooInfoController.GetAllFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());

            var feed = new WallFeed
            {
                id = default(int),
                title = "Purim Events",
                info = "Our Purim Events started!",
                language = (int)Languages.en
            };

            ZooInfoController.UpdateWallFeed(feed, false, true);
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
                title = "title",
                info = "Our Purim Events started!",
                language = nonExistantLangauge
            };

            ZooInfoController.UpdateWallFeed(feed, false, true);
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
                title = "KAKI!",
                info = "   ",
                language = (int)Languages.en
            };

            ZooInfoController.UpdateWallFeed(feed, false, true);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateWallFeedWrongTitleEmptySpaces()
        {
            var feeds = ZooInfoController.GetAllFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());

            var feed = new WallFeed
            {
                id = default(int),
                title = "   ",
                info = "KAKI!",
                language = (int)Languages.en
            };

            ZooInfoController.UpdateWallFeed(feed, false, true);
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
                title = "KAKI!",
                info = "",
                language = (int)Languages.en
            };

            ZooInfoController.UpdateWallFeed(feed, false, true);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateWallFeedWrongTitleEmpty()
        {
            var feeds = ZooInfoController.GetAllFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());

            var feed = new WallFeed
            {
                id = default(int),
                title = "",
                info = "KAKI!",
                language = (int)Languages.en
            };

            ZooInfoController.UpdateWallFeed(feed, false, true);
        }

        [TestMethod]
        public void UpdateWallFeedValidInput()
        {
            var feeds = ZooInfoController.GetAllFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());

            var feed = new WallFeed
            {
                id = 1,
                title = "Purim is back again!",
                info = "And we are here to take your money!",
                language = (int)Languages.en
            };

            ZooInfoController.UpdateWallFeed(feed, false, true);

            feeds = ZooInfoController.GetAllFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());
            Assert.IsTrue(feeds.Any(f => f.info == feed.info));
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateWallFeedInfoAndTitleAlreadyExists()
        {
            var feeds = ZooInfoController.GetAllFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());

            var feed = new WallFeed
            {
                id = 1,
                title = "Sukut Events",
                info = "Our Sukut Events started!",
                language = (int)Languages.en
            };

            ZooInfoController.UpdateWallFeed(feed, false, true);
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
                title = "Sukut Events",
                info = "Our Sukut Events started!",
                language = (int)Languages.en
            };

            ZooInfoController.UpdateWallFeed(feed, false, true);
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

        #region GeneralInforamtion

        #region GetAboutUs
        [TestMethod]
        public void GetAboutUsValidInput()
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

        #region UpdateAboutUs
        [TestMethod]
        public void UpdateAboutUsValidInput()
        {
            var aboutUs = ZooInfoController.GetZooAboutInfo((int)Languages.en);
            Assert.AreEqual(aboutUs.Count(), 1);

            var oldAboutUs = aboutUs.SingleOrDefault(au => au.AboutUs == "We are Negev Zoo!!! We love animals");
            var newAboutUs = "This is the new about us!";

            ZooInfoController.UpdateZooAboutInfo(newAboutUs, (int)Languages.en);

            aboutUs = ZooInfoController.GetZooAboutInfo((int)Languages.en);
            Assert.IsTrue(aboutUs.Any(au => au.AboutUs == newAboutUs));
            Assert.IsFalse(aboutUs.Any(au => au.AboutUs == oldAboutUs.AboutUs));
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

        #region GetOpeningHourNote
        [TestMethod]
        public void GetOpeningHourNoteValidInput()
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

        #region UpdateOpeningHourNote
        [TestMethod]
        public void UpdateOpeningHourNoteValidInput()
        {
            var ohNote = ZooInfoController.GetOpeningHourNote((int)Languages.en);
            Assert.AreEqual(ohNote.Count(), 1);

            var oldNote = ohNote.SingleOrDefault(ohn => ohn.OpeningHourNote == "The cashier desk will bew closed two hours before the zoo is closing.");
            var newOpeningHournote = "This is the new note!";

            ZooInfoController.UpdateOpeningHourNote(newOpeningHournote, (int)Languages.en);

            ohNote = ZooInfoController.GetOpeningHourNote((int)Languages.en);
            Assert.IsTrue(ohNote.Any(ohn => ohn.OpeningHourNote == newOpeningHournote));
            Assert.IsFalse(ohNote.Any(ohn => ohn.OpeningHourNote == oldNote.OpeningHourNote));
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

        #region GetContactInfoNote
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

        #region UpdateOpeningHourNote
        [TestMethod]
        public void UpdateContactInfoNoteValidInput()
        {
            var ciNote = ZooInfoController.GetContactInfoNote((int)Languages.en);
            Assert.AreEqual(ciNote.Count(), 1);

            var oldNote = ciNote.SingleOrDefault(cin => cin.ContactInfoNote == "Contact between 08:00 - 22:00");
            var newContactInfoNote = "This is the new note!";

            ZooInfoController.UpdateContactInfoNote(newContactInfoNote, (int)Languages.en);

            ciNote = ZooInfoController.GetContactInfoNote((int)Languages.en);
            Assert.IsTrue(ciNote.Any(cin => cin.ContactInfoNote == newContactInfoNote));
            Assert.IsFalse(ciNote.Any(cin => cin.ContactInfoNote == oldNote.ContactInfoNote));
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
        
        [TestMethod]
        public void GetMapUrl()
        {
            var mapUrl = ZooInfoController.GetMapUrl();
            Assert.AreEqual(mapUrl.Count(), 1);
            Assert.AreEqual("MapUrl", mapUrl.First().Url);
        }

        [TestMethod]
        public void GetAllLanagues()
        {
            var Languages = ZooInfoController.GetAllLanguages();
            Assert.AreEqual(Languages.Count(), 4);
            Assert.IsTrue(Languages.Any(l => l.name == "English"));
            Assert.IsFalse(Languages.Any(l => l.name == "Jibrish"));
        }
        #endregion
    }
}
