using BL;
using DAL;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Linq;

namespace ZooTests.Unit_Tests
{
    [TestClass]
    public class ZooInfoUnitTests
    {
        private ZooContext context;
        private int nonExistantLangauge;

        #region SetUp and TearDown
        [TestInitialize]
        public void SetUp()
        {
            context = new ZooContext();
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
            var prices = context.GetAllPrices(1);
            Assert.IsInstanceOfType(prices, typeof(Price[]));

            Assert.AreEqual(5, prices.Count());
            Assert.IsTrue(prices.Any(p => p.population == "מבוגר"));
        }

        [TestMethod]
        public void GetAllPricesLangEng()
        {
            var prices = context.GetAllPrices(2);
            Assert.IsInstanceOfType(prices, typeof(Price[]));

            Assert.AreEqual(5, prices.Count());
            Assert.IsTrue(prices.Any(p => p.population == "Adult"));
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Wrong language")]
        public void GetAllPricesLanguageNotExist()
        {
            var prices = context.GetAllPrices(nonExistantLangauge);

            Assert.AreEqual(prices.Count(), 0);
        }
        #endregion

        #region updatePrices
        [TestMethod]
        public void UpdatePriceAddPriceValidTest()
        {
            var prices = context.GetAllPrices((int)Languages.en);
            Assert.AreEqual(5, prices.Count());

            var price = new Price
            {
                id = default(int),
                population = "Veteran",
                pricePop = 15.5,
                language = (int)Languages.en
            };

            context.UpdatePrice(price);

            prices = context.GetAllPrices((int)Languages.en);
            Assert.AreEqual(6, prices.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input while adding price. Price population already exists.")]
        public void UpdatePriceAddPricePopulationAlreadyExists()
        {
            var prices = context.GetAllPrices((int)Languages.en);
            Assert.AreEqual(5, prices.Count());

            var price = new Price
            {
                id = default(int),
                population = "Adult",
                pricePop = 15.5,
                language = (int)Languages.en
            };

            context.UpdatePrice(price);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong Input. Wrong Language")]
        public void UpdatePriceWrongLang()
        {
            var prices = context.GetAllPrices((int)Languages.en);
            Assert.AreEqual(5, prices.Count());

            var price = new Price
            {
                id = default(int),
                population = "Veteran",
                pricePop = 15.5,
                language = nonExistantLangauge
            };

            context.UpdatePrice(price);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The price population is null or whitespaces")]
        public void UpdatePriceWrongPopulationWhiteSpaces()
        {
            var prices = context.GetAllPrices((int)Languages.en);
            Assert.AreEqual(5, prices.Count());

            var price = new Price
            {
                id = default(int),
                population = "   ",
                pricePop = 15.5,
                language = (int)Languages.en
            };

            context.UpdatePrice(price);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The price population is null or whitespaces")]
        public void UpdatePriceWrongPopulationEmpty()
        {
            var prices = context.GetAllPrices((int)Languages.en);
            Assert.AreEqual(5, prices.Count());

            var price = new Price
            {
                id = default(int),
                population = "",
                pricePop = 15.5,
                language = (int)Languages.en
            };

            context.UpdatePrice(price);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input, The price amount is lower than 0")]
        public void UpdatePriceWrongPrice()
        {
            var prices = context.GetAllPrices((int)Languages.en);
            Assert.AreEqual(5, prices.Count());

            var price = new Price
            {
                id = default(int),
                population = "Veteran",
                pricePop = -15.5,
                language = (int)Languages.en
            };

            context.UpdatePrice(price);
        }

        [TestMethod]
        public void UpdatePriceValidTest()
        {
            var prices = context.GetAllPrices((int)Languages.en);
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

            context.UpdatePrice(price);

            prices = context.GetAllPrices((int)Languages.en);
            Assert.AreEqual(5, prices.Count());
            Assert.IsTrue(prices.Any(p => p.population == price.population && p.pricePop == price.pricePop));
            Assert.IsFalse(prices.Any(p => p.population == price.population && p.pricePop == oldPrice));
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input while updating price. Price population already exists.")]
        public void UpdatePricePopulationAlreadyExists()
        {
            var prices = context.GetAllPrices((int)Languages.en);
            Assert.AreEqual(5, prices.Count());

            var price = new Price
            {
                id = 9,
                population = "Student",
                pricePop = 10,
                language = (int)Languages.en
            };

            price.population = "Adult";

            context.UpdatePrice(price);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The price id doesn't exist")]
        public void UpdatePricePopulationIdDoesntExists()
        {
            var prices = context.GetAllPrices((int)Languages.en);
            Assert.AreEqual(5, prices.Count());

            var price = new Price
            {
                id = -2,
                population = "Student",
                pricePop = 10,
                language = (int)Languages.en
            };

            context.UpdatePrice(price);
        }
        #endregion

        #region DeletePrice
        [TestMethod]
        public void DeletePriceValidInput()
        {
            var prices = context.GetAllPrices((int)Languages.he);
            Assert.AreEqual(5, prices.Count());

            var price = prices.SingleOrDefault(p => p.population == "סטודנט");
            Assert.IsNotNull(price);

            context.DeletePrice(price.id);

            prices = context.GetAllPrices((int)Languages.he);
            Assert.AreEqual(4, prices.Count());
            Assert.IsFalse(prices.Any(p => p.population == "סטודנט"));

            prices = context.GetAllPrices((int)Languages.en);
            Assert.AreEqual(5, prices.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Price doesn't exists.")]
        public void DeletePriceIdDoesntExists()
        {
            context.DeletePrice(-4);
        }
        #endregion

        #endregion

        #region OpeningHour

        #region GetAllOpeningHourResults

        [TestMethod]
        public void GetAllOpeningHourResults()
        {
            var openingHours = context.GetAllOpeningHours((int)Languages.he);

            Assert.AreEqual(6, openingHours.Count());
            Assert.IsTrue(openingHours.Any(oh => oh.Day == "ראשון"));
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Wrong language")]
        public void GetAllOpeningHourReusltsLangDoeantExists()
        {
            context.GetAllOpeningHours(nonExistantLangauge);
        }

        #endregion

        #region GetAllOpeningHourResults

        [TestMethod]
        public void GetAllOpeningHourTypes()
        {
            var openingHours = context.GetAllOpeningHoursType();

            Assert.AreEqual(6, openingHours.Count());
            Assert.IsTrue(openingHours.Any(oh => oh.day == 1));
        }

        #endregion

        #region UpdateOpeningHour
        [TestMethod]
        public void UpdateOpeningHourAddValidInput()
        {
            var openingHours = context.GetAllOpeningHoursType();

            Assert.AreEqual(6, openingHours.Count());
            Assert.IsTrue(openingHours.Any(oh => oh.day == 1));
            Assert.IsFalse(openingHours.Any(oh => oh.day == 6));

            var openingHourResultHeb = context.GetAllOpeningHours((int)Languages.he);
            Assert.IsFalse(openingHourResultHeb.Any(oh => oh.Day == "שישי"));

            var openingHourResultEn = context.GetAllOpeningHours((int)Languages.en);
            Assert.IsFalse(openingHourResultHeb.Any(oh => oh.Day == "Friday"));

            OpeningHour opHour = new OpeningHour
            {
                id = default(int),
                day = 6,
                startTime = new TimeSpan(9, 45, 0),
                endTime = new TimeSpan(18, 0, 0)
            };

            context.UpdateOpeningHour(opHour);

            openingHours = context.GetAllOpeningHoursType();

            Assert.AreEqual(7, openingHours.Count());
            Assert.IsTrue(openingHours.Any(oh => oh.day == 6));

            //heb
            openingHourResultHeb = context.GetAllOpeningHours((int)Languages.he);
            Assert.AreEqual(7, openingHourResultHeb.Count());
            Assert.IsTrue(openingHourResultHeb.Any(oh => oh.Day == "שישי"));

            openingHourResultEn = context.GetAllOpeningHours((int)Languages.en);
            Assert.AreEqual(7, openingHourResultEn.Count());
            Assert.IsTrue(openingHourResultEn.Any(oh => oh.Day == "Friday"));
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input while adding Opening hour. The day of the opening hour is already exsists")]
        public void UpdateOpeningHourAddDayAlreadyExists()
        {
            var openingHours = context.GetAllOpeningHours((int)Languages.he);

            Assert.AreEqual(6, openingHours.Count());
            Assert.IsTrue(openingHours.Any(oh => oh.Day == "ראשון"));

            OpeningHour opHour = new OpeningHour
            {
                id = default(int),
                day = 7,
                startTime = new TimeSpan(9, 45, 0),
                endTime = new TimeSpan(18, 0, 0),
            };

            context.UpdateOpeningHour(opHour);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The start time is later than the end time.")]
        public void UpdateOpeningHourWrongTime()
        {
            var openingHours = context.GetAllOpeningHours((int)Languages.he);

            Assert.AreEqual(6, openingHours.Count());
            Assert.IsTrue(openingHours.Any(oh => oh.Day == "ראשון"));

            OpeningHour opHour = new OpeningHour
            {
                id = default(int),
                day = 5,
                startTime = new TimeSpan(18, 0, 0),
                endTime = new TimeSpan(9, 45, 0),
            };

            context.UpdateOpeningHour(opHour);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The day is empty or null")]
        public void UpdateOpeningHourWrongDay()
        {
            var openingHours = context.GetAllOpeningHours((int)Languages.he);

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

            context.UpdateOpeningHour(opHour);
        }

        [TestMethod]
        public void UpdateOpeningHour()
        {
            var openingHours = context.GetAllOpeningHoursType();
            Assert.AreEqual(6, openingHours.Count());
            Assert.IsTrue(openingHours.Any(oh => oh.day == 1));

            var openingHourResultHeb = context.GetAllOpeningHours((int)Languages.he);
            Assert.IsTrue(openingHourResultHeb.Any(oh => oh.Day == "שבת"));

            var openingHourResultEn = context.GetAllOpeningHours((int)Languages.en);
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

            context.UpdateOpeningHour(opHour);

            openingHours = context.GetAllOpeningHoursType();

            opHour = openingHours.SingleOrDefault(oh => oh.day == 7);
            Assert.IsNotNull(opHour);
            Assert.AreEqual(newTime, opHour.endTime);

            //heb
            openingHourResultHeb = context.GetAllOpeningHours((int)Languages.he);
            Assert.AreEqual(6, openingHourResultHeb.Count());
            var fridayHeb = openingHourResultHeb.SingleOrDefault(oh => oh.Day == "שבת");
            Assert.IsNotNull(fridayHeb);
            Assert.AreEqual(newTime, fridayHeb.EndTime);

            openingHourResultEn = context.GetAllOpeningHours((int)Languages.en);
            Assert.AreEqual(6, openingHourResultEn.Count());
            var fridayEn = (openingHourResultEn.SingleOrDefault(oh => oh.Day == "Saturday"));
            Assert.IsNotNull(fridayEn);
            Assert.AreEqual(newTime, fridayEn.EndTime);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input while updating Opening hour. The Opening hour day already exists")]
        public void UpdateOpeningHourDayAlreadyExists()
        {
            var openingHours = context.GetAllOpeningHours((int)Languages.he);
            Assert.AreEqual(6, openingHours.Count());

            OpeningHour opHour = new OpeningHour
            {
                id = 1,
                day = 2,
                startTime = new TimeSpan(11, 30, 00),
                endTime = new TimeSpan(12, 0, 0),
            };

            context.UpdateOpeningHour(opHour);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The opening hour id doesn't exists.")]
        public void UpdateOpeningHourIdDoesntExists()
        {
            var openingHours = context.GetAllOpeningHours((int)Languages.he);
            Assert.AreEqual(6, openingHours.Count());

            OpeningHour opHour = new OpeningHour
            {
                id = -11,
                day = 7,
                startTime = new TimeSpan(10, 45, 0),
                endTime = new TimeSpan(18, 0, 0),
            };

            context.UpdateOpeningHour(opHour);
        }

        #endregion

        #region DeleteOpeningHour

        [TestMethod]
        public void DeleteOpeningHourValidInput()
        {
            var openingHours = context.GetAllOpeningHoursType();
            Assert.AreEqual(6, openingHours.Count());

            //hebrew
            var openingHourResultsHeb = context.GetAllOpeningHours((int)Languages.he);
            Assert.AreEqual(6, openingHourResultsHeb.Count());

            //english
            var openingHourResultsEn = context.GetAllOpeningHours((int)Languages.en);
            Assert.AreEqual(6, openingHourResultsEn.Count());

            OpeningHour opHour = openingHours.SingleOrDefault(oh => oh.day == 1);

            context.DeleteOpeningHour(opHour.id);

            openingHours = context.GetAllOpeningHoursType();
            Assert.AreEqual(5, openingHours.Count());

            //hebrew
            openingHourResultsHeb = context.GetAllOpeningHours((int)Languages.he);
            Assert.AreEqual(5, openingHourResultsHeb.Count());
            Assert.IsFalse(openingHourResultsHeb.Any(oh => oh.Day == "ראשון"));

            //english
            openingHourResultsEn = context.GetAllOpeningHours((int)Languages.en);
            Assert.AreEqual(5, openingHourResultsEn.Count());
            Assert.IsFalse(openingHourResultsEn.Any(oh => oh.Day == "Sunday"));
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Opening hour id doesn't exsists.")]
        public void DeleteOpeningHourIdDoesntExists()
        {
            context.DeleteOpeningHour(-4);
        }
        #endregion

        #endregion

        #region ContactInfo

        #region GetAllContactInfo

        [TestMethod]
        public void GetAllContactInfos()
        {
            var contactInfos = context.GetAllContactInfos((int)Languages.he);
            Assert.AreEqual(3, contactInfos.Count());
            Assert.IsTrue(contactInfos.Any(ci => ci.via == "דואר"));
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Wrong language")]
        public void GetAllContactInfosLanguageNotExists()
        {
            context.GetAllContactInfos(nonExistantLangauge);
        }

        #endregion

        #region UpdateContactInfo
        [TestMethod]
        public void UpdateContactAddValidInput()
        {
            var contactInfos = context.GetAllContactInfos((int)Languages.en);
            Assert.AreEqual(3, contactInfos.Count());

            ContactInfo contact = new ContactInfo
            {
                id = default(int),
                via = "E-mail",
                address = "avivmag@post.bgu.ac.il",
                language = (int)Languages.en
            };

            context.UpdateContactInfo(contact);

            contactInfos = context.GetAllContactInfos((int)Languages.en);
            Assert.AreEqual(4, contactInfos.Count());

            contactInfos = context.GetAllContactInfos((int)Languages.he);
            Assert.AreEqual(3, contactInfos.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input while adding contactInfo. Contact info address and Via already exists.")]
        public void UpdateContactAddAddresAndViaExists()
        {
            var contactInfos = context.GetAllContactInfos((int)Languages.en);
            Assert.AreEqual(3, contactInfos.Count());

            ContactInfo contact = new ContactInfo
            {
                id = default(int),
                via = "E-Mail",
                address = "gilorisr@post.bgu.ac.il",
                language = (int)Languages.en
            };

            context.UpdateContactInfo(contact);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. ContactInfo's via is whitespaces or null")]
        public void UpdateContactViaEmpty()
        {
            var contactInfos = context.GetAllContactInfos((int)Languages.en);
            Assert.AreEqual(3, contactInfos.Count());

            ContactInfo contact = new ContactInfo
            {
                id = default(int),
                via = "",
                address = "avivmag@post.bgu.ac.il",
                language = (int)Languages.en
            };

            context.UpdateContactInfo(contact);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. ContactInfo's via is whitespaces or null")]
        public void UpdateContactViaWhiteSpaces()
        {
            var contactInfos = context.GetAllContactInfos((int)Languages.en);
            Assert.AreEqual(3, contactInfos.Count());

            ContactInfo contact = new ContactInfo
            {
                id = default(int),
                via = "     ",
                address = "avivmag@post.bgu.ac.il",
                language = (int)Languages.en
            };

            context.UpdateContactInfo(contact);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. ContactInfo's address is whitespaces or null")]
        public void UpdateContactAddressEmpty()
        {
            var contactInfos = context.GetAllContactInfos((int)Languages.en);
            Assert.AreEqual(3, contactInfos.Count());

            ContactInfo contact = new ContactInfo
            {
                id = default(int),
                via = "E-mail",
                address = "",
                language = (int)Languages.en
            };

            context.UpdateContactInfo(contact);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. ContactInfo's address is whitespaces or null")]
        public void UpdateContactAddressWhiteSpaces()
        {
            var contactInfos = context.GetAllContactInfos((int)Languages.en);
            Assert.AreEqual(3, contactInfos.Count());

            ContactInfo contact = new ContactInfo
            {
                id = default(int),
                via = "E-mail",
                address = "         ",
                language = (int)Languages.en
            };

            context.UpdateContactInfo(contact);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Wrong language")]
        public void UpdateContactWrongLanguage()
        {
            var contactInfos = context.GetAllContactInfos((int)Languages.en);
            Assert.AreEqual(3, contactInfos.Count());

            ContactInfo contact = new ContactInfo
            {
                id = default(int),
                via = "E-mail",
                address = "This is address",
                language = nonExistantLangauge
            };

            context.UpdateContactInfo(contact);
        }

        [TestMethod]
        public void UpdateContactValidInput()
        {
            var contactInfos = context.GetAllContactInfos((int)Languages.he);
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

            context.UpdateContactInfo(contact);

            contactInfos = context.GetAllContactInfos((int)Languages.he);
            Assert.AreEqual(3, contactInfos.Count());
            Assert.IsTrue(contactInfos.Any(ci => ci.address == contact.address));
            Assert.IsFalse(contactInfos.Any(ci => ci.address == oldAddress));
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input while updating contactInfo. Contact info address and Via already exists.")]
        public void UpdateContactViaAndAddressAlreadyExists()
        {
            var contactInfos = context.GetAllContactInfos((int)Languages.he);
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

            context.UpdateContactInfo(contact);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The ContactInfo's id doesn't exists")]
        public void UpdateContactIdDoesntExists()
        {
            var contactInfos = context.GetAllContactInfos((int)Languages.he);
            Assert.AreEqual(3, contactInfos.Count());

            ContactInfo contact = new ContactInfo
            {
                id = 3,
                via = "דואר",
                address = "דרך אילן רמון 5, באר שבע",
                language = (int)Languages.he
            };

            contact.id = -3;

            context.UpdateContactInfo(contact);
        }
        #endregion

        #region DeleteContactInfo
        [TestMethod]
        public void DeleteContactInfoValidInput()
        {
            var contactInfos = context.GetAllContactInfos((int)Languages.en);
            Assert.AreEqual(3, contactInfos.Count());

            ContactInfo contact = contactInfos.SingleOrDefault(ci => ci.via == "Post Mail");

            context.DeleteContactInfo(contact.id);

            Assert.AreEqual(3, contactInfos.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. ContactInfo id doesn't exists")]
        public void DeleteContactInfoIdDoesntExists()
        {
            context.DeleteContactInfo(-4);
        }
        #endregion

        #endregion

        #region Special event

        #region GetAllSpecialEvents

        [TestMethod]
        public void GetAllSpecialEvents()
        {
            var specialEvents = context.GetAllSpecialEvents((int)Languages.he);
            Assert.AreEqual(2, specialEvents.Count());
            Assert.IsTrue(specialEvents.Any(se => se.description == "1קקי"));
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Wrong language.")]
        public void GetAllSpecialEventsLanguageNotExists()
        {
            context.GetAllSpecialEvents(nonExistantLangauge);
        }
        #endregion
        
        #region UpdateSpecialEvent

        [TestMethod]
        public void UpdateSpecialEventsAddValidInput()
        {
            var specialEvents = context.GetAllSpecialEvents((int)Languages.en);
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

            context.UpdateSpecialEvent(eve, false);

            specialEvents = context.GetAllSpecialEvents((int)Languages.en);
            Assert.AreEqual(3, specialEvents.Count());

            specialEvents = context.GetAllSpecialEvents((int)Languages.he);
            Assert.AreEqual(2, specialEvents.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The description is null or white space")]
        public void UpdateSpecialEventAddDescriptionExists()
        {
            var specialEvents = context.GetAllSpecialEvents((int)Languages.en);
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

            context.UpdateSpecialEvent(eve, false);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The end date is earlier than the start date.")]
        public void UpdateSpecailEventsInvalidDates()
        {
            var specialEvents = context.GetAllSpecialEvents((int)Languages.en);
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

            context.UpdateSpecialEvent(eve, false);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The title is null or white space")]
        public void UpdateSpecailEventsEmptyTitle()
        {
            var specialEvents = context.GetAllSpecialEvents((int)Languages.en);
            Assert.AreEqual(2, specialEvents.Count());

            SpecialEvent eve = new SpecialEvent
            {
                id = default(int),
                title = "",
                description = "We have a Kaytana",
                startDate = new DateTime(2018, 7, 1),
                endDate = new DateTime(2018, 9, 1),
                language = (int)Languages.en
            };

            context.UpdateSpecialEvent(eve, false);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The description is null or white space")]
        public void UpdateSpecailEventsWhiteSpacesDescription()
        {
            var specialEvents = context.GetAllSpecialEvents((int)Languages.en);
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

            context.UpdateSpecialEvent(eve, false);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Wrong language")]
        public void UpdateSpecialEventsWrongLanguage()
        {
            var specialEvents = context.GetAllSpecialEvents((int)Languages.en);
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

            context.UpdateSpecialEvent(eve, false);
        }

        [TestMethod]
        public void UpdateSpecialEventsValidInput()
        {
            var specialEvents = context.GetAllSpecialEvents((int)Languages.en);
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

            context.UpdateSpecialEvent(eve, false);

            specialEvents = context.GetAllSpecialEvents((int)Languages.en);
            Assert.AreEqual(2, specialEvents.Count());
            Assert.IsTrue(specialEvents.Any(se => se.description == eve.description));
            Assert.IsFalse(specialEvents.Any(se => se.description == oldDescription));
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input While updating SpecialEvent. The SpecialEvent descroption already exists.")]
        public void UpdateSpecialEventsDescriptionAlreadyExists()
        {
            var specialEvents = context.GetAllSpecialEvents((int)Languages.en);
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

            context.UpdateSpecialEvent(eve, false);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The SpecialEvent's id doesn't exists")]
        public void UpdateSpecialEventsIdDoesntExists()
        {
            var specialEvents = context.GetAllSpecialEvents((int)Languages.he);
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

            context.UpdateSpecialEvent(eve, false);
        }
        #endregion

        #region DeleteSpecialEvent

        [TestMethod]
        public void DeleteSpecialEventValidInput()
        {
            var specialEvents = context.GetAllSpecialEvents((int)Languages.he);
            Assert.AreEqual(2, specialEvents.Count());

            SpecialEvent eve = specialEvents.SingleOrDefault(se => se.title == "אירועי פורים");

            context.DeleteSpecialEvent(eve.id);

            specialEvents = context.GetAllSpecialEvents((int)Languages.he);
            Assert.AreEqual(1, specialEvents.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. SpecialEvent's id doesn't exists")]
        public void DeleteSpecialEventsIdDoesntExists()
        {
            context.DeleteSpecialEvent(-4);
        }
        #endregion

        #endregion

        #region feeds

        #region GetAllWallFeed
        [TestMethod]
        public void GetAllWallFeedLangHe()
        {
            Assert.AreEqual(16, context.GetAllWallFeeds(1).Count());
        }

        [TestMethod]
        public void GetAllWallFeedLangEng()
        {
            Assert.AreEqual(16, context.GetAllWallFeeds(2).Count());
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Wrong language")]
        public void GetAllWallFeedLanguageNotExist()
        {
            Assert.AreEqual(context.GetAllWallFeeds(nonExistantLangauge).Count(), 0);
        }

        #endregion

        #region UpdateFeedWall
        [TestMethod]
        public void UpdateWallFeedAddFeedValidTest()
        {
            var feeds = context.GetAllWallFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());

            var feed = new WallFeed
            {
                id = default(int),
                title = "KAKI!!",
                info = "Another Kaki appears in the zoo",
                language = (int)Languages.en
            };

            context.UpdateWallFeed(feed, false, true);

            feeds = context.GetAllWallFeeds((int)Languages.en);
            Assert.AreEqual(17, feeds.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input while adding WallFeed. The WallFeed info and title are already exists.")]
        public void UpdateWallFeedAddFeedInfoAndTitleAlreadyExists()
        {
            var feeds = context.GetAllWallFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());

            var feed = new WallFeed
            {
                id = default(int),
                title = "Purim Events",
                info = "Our Purim Events started!",
                language = (int)Languages.en
            };

            context.UpdateWallFeed(feed, false, true);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Wrong language")]
        public void UpdateWallFeedLanguageDoesntExists()
        {
            var feeds = context.GetAllWallFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());

            var feed = new WallFeed
            {
                id = default(int),
                title = "title",
                info = "Our Purim Events started!",
                language = nonExistantLangauge
            };

            context.UpdateWallFeed(feed, false, true);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The info is null or white space")]
        public void UpdateWallFeedWrongInfoEmptySpaces()
        {
            var feeds = context.GetAllWallFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());

            var feed = new WallFeed
            {
                id = default(int),
                title = "KAKI!",
                info = "   ",
                language = (int)Languages.en
            };

            context.UpdateWallFeed(feed, false, true);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The title is null or white space")]
        public void UpdateWallFeedWrongTitleEmptySpaces()
        {
            var feeds = context.GetAllWallFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());

            var feed = new WallFeed
            {
                id = default(int),
                title = "   ",
                info = "KAKI!",
                language = (int)Languages.en
            };

            context.UpdateWallFeed(feed, false, true);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The info is null or white space")]
        public void UpdateWallFeedWrongInfoEmpty()
        {
            var feeds = context.GetAllWallFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());

            var feed = new WallFeed
            {
                id = default(int),
                title = "KAKI!",
                info = "",
                language = (int)Languages.en
            };

            context.UpdateWallFeed(feed, false, true);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The title is null or white space")]
        public void UpdateWallFeedWrongTitleEmpty()
        {
            var feeds = context.GetAllWallFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());

            var feed = new WallFeed
            {
                id = default(int),
                title = "",
                info = "KAKI!",
                language = (int)Languages.en
            };

            context.UpdateWallFeed(feed, false, true);
        }

        [TestMethod]
        public void UpdateWallFeedValidInput()
        {
            var feeds = context.GetAllWallFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());

            var feed = new WallFeed
            {
                id = 1,
                title = "Purim is back again!",
                info = "And we are here to take your money!",
                language = (int)Languages.en
            };

            context.UpdateWallFeed(feed, false, true);

            feeds = context.GetAllWallFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());
            Assert.IsTrue(feeds.Any(f => f.info == feed.info));
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input while updating WallFeed. The WallFeed Info and title are already exists")]
        public void UpdateWallFeedInfoAndTitleAlreadyExists()
        {
            var feeds = context.GetAllWallFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());

            var feed = new WallFeed
            {
                id = 1,
                title = "Sukut Events",
                info = "Our Sukut Events started!",
                language = (int)Languages.en
            };

            context.UpdateWallFeed(feed, false, true);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The WallFeed's id doesn't exists")]
        public void UpdateWallFeedIdDoesntExists()
        {
            var feeds = context.GetAllWallFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());

            var feed = new WallFeed
            {
                id = -4,
                created = new DateTime(2018, 03, 05),
                title = "Sukut Events",
                info = "Our Sukut Events started!",
                language = (int)Languages.en
            };

            context.UpdateWallFeed(feed, false, true);
        }
        #endregion

        #region deleteWallFeed

        [TestMethod]
        public void DeleteWallFeedValidInput()
        {
            var feeds = context.GetAllWallFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());

            var firstFeed = feeds.SingleOrDefault(f => f.id == 1);
            Assert.IsNotNull(firstFeed);

            context.DeleteWallFeed((int)firstFeed.id);

            feeds = context.GetAllWallFeeds((int)Languages.en);
            Assert.AreEqual(15, feeds.Count());

            feeds = context.GetAllWallFeeds((int)Languages.he);
            Assert.AreEqual(16, feeds.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. WallFeed's id doesn't exists")]
        public void DeleteWallFeedIdDoesntExists()
        {
            var feeds = context.GetAllWallFeeds((int)Languages.en);
            Assert.AreEqual(16, feeds.Count());

            context.DeleteWallFeed(-4);
        }
        #endregion

        #endregion

        #region GeneralInforamtion

        #region GetAboutUs
        [TestMethod]
        public void GetAboutUsValidInput()
        {
            var aboutUs = context.GetZooAboutInfo(1);
            Assert.IsInstanceOfType(aboutUs, typeof(String));
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Wrong language")]
        public void GetAboutUsLangaugeNotExist()
        {
            context.GetZooAboutInfo(nonExistantLangauge);
        }
        #endregion

        #region UpdateAboutUs

        [TestMethod]
        public void UpdateAboutUsValidInput()
        {
            var aboutUs = context.GetZooAboutInfo((int)Languages.en);
            Assert.AreEqual(aboutUs, "We are Negev Zoo!!! We love animals");

            var oldAboutUs = aboutUs;
            var newAboutUs = "This is the new about us!";

            context.UpdateZooAboutInfo(newAboutUs, (int)Languages.en);

            aboutUs = context.GetZooAboutInfo((int)Languages.en);
            Assert.AreEqual(aboutUs, newAboutUs);
            Assert.AreNotEqual(aboutUs, oldAboutUs);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The info is empty or null")]
        public void UpdateAboutUsWrongAboutUsWhiteSpaces()
        {
            context.UpdateZooAboutInfo("      ", (int)Languages.en);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The info is empty or null")]
        public void UpdateAboutUsWrongAboutUsEmpty()
        {
            context.UpdateZooAboutInfo("", (int)Languages.en);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Wrong language")]
        public void UpdateAboutUsLangaugeNotExist()
        {
            context.UpdateZooAboutInfo("This is the new about us !", nonExistantLangauge);
        }
        #endregion

        #region GetOpeningHourNote

        [TestMethod]
        public void GetOpeningHourNoteValidTest()
        {
            var note = context.GetOpeningHourNote(1);
            Assert.AreEqual("הקופות יסגרו שעתיים לפני סגירת הגן", note);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Wrong language")]
        public void GetOpeningHourNoteLangaugeNotExist()
        {
            context.GetOpeningHourNote(nonExistantLangauge);
        }

        #endregion

        #region UpdateOpeningHourNote

        [TestMethod]
        public void UpdateOpeningHourNoteValidInput()
        {
            var ohNote = context.GetOpeningHourNote((int)Languages.en);

            var oldNote = ohNote;
            var newAboutUs = "This is the new note!";

            context.UpdateOpeningHourNote(newAboutUs, (int)Languages.en);

            ohNote = context.GetOpeningHourNote((int)Languages.en);
            Assert.IsTrue(ohNote == newAboutUs);
            Assert.IsFalse(ohNote == oldNote);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The note is empty or null")]
        public void UpdateOpeningHourNoteWrongAboutUsWhiteSpaces()
        {
            context.UpdateOpeningHourNote("      ", (int)Languages.en);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The note is empty or null")]
        public void UpdateOpeningHourNoteWrongAboutUsEmpty()
        {
            context.UpdateOpeningHourNote("", (int)Languages.en);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Wrong language")]
        public void UpdateOpeningHourNoteLangaugeNotExist()
        {
            context.UpdateOpeningHourNote("This is the new note!", nonExistantLangauge);
        }

        #endregion

        #region GetContactInfoNote

        [TestMethod]
        public void GetContactInfoNoteValidTest()
        {
            var note = context.GetContactInfoNote(1);
            Assert.AreEqual("ניתן ליצור קשר בין השעות 08:00 לבין 22:00", note);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Wrong language")]
        public void GetContactInfoNoteLangaugeNotExist()
        {
            context.GetContactInfoNote(nonExistantLangauge);
        }

        #endregion

        #region UpdateOpeningHourNote

        [TestMethod]
        public void UpdateContactInfoNoteValidInput()
        {
            var ciNote = context.GetContactInfoNote((int)Languages.en);

            var oldNote = ciNote;
            var newAboutUs = "This is the new note!";

            context.UpdateContactInfoNote(newAboutUs, (int)Languages.en);

            ciNote = context.GetContactInfoNote((int)Languages.en);
            Assert.IsTrue(ciNote == newAboutUs);
            Assert.IsFalse(ciNote == oldNote);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The note is empty spaces or null")]
        public void UpdateContactInfoNoteWrongAboutUsWhiteSpaces()
        {
            context.UpdateContactInfoNote("      ", (int)Languages.en);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. The note is empty spaces or null")]
        public void UpdateContactInfoNoteWrongAboutUsEmpty()
        {
            context.UpdateContactInfoNote("", (int)Languages.en);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), "Wrong input. Wrong language")]
        public void UpdateContactInfoNoteLangaugeNotExist()
        {
            context.UpdateContactInfoNote("This is the new note!", nonExistantLangauge);
        }

        #endregion

        [TestMethod]
        public void GetMapUrl()
        {
            var mapUrl = context.GetMapUrl();
            Assert.AreEqual(mapUrl.Count(), 1);
            Assert.AreEqual("MapUrl", mapUrl.First());
        }

        [TestMethod]
        public void GetAllLanagues()
        {
            var Languages = context.GetAllLanguages();
            Assert.AreEqual(Languages.Count(), 4);
            Assert.IsTrue(Languages.Any(l => l.name == "English"));
            Assert.IsFalse(Languages.Any(l => l.name == "Jibrish"));
        }
        #endregion
    }
}
