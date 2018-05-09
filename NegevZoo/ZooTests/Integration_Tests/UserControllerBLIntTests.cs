using System;
using System.Linq;
using System.Web.Http;
using DAL;
using BL;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using NegevZoo.Controllers;
using System.Net.Http;

namespace ZooTests
{
    [TestClass]
    public class UserControllerBlIntegrationTests
    {

        #region SetUp and TearDown

        private UserController usersController;

        [TestInitialize]
        public void SetUp()
        {
            // The line below must be in every setup of each test. otherwise it will not be in a testing environment.
            ControllerBase.isTesting = true;
            usersController = new UserController();
            usersController.Request = new HttpRequestMessage();
            usersController.Request.Headers.Add("Cookie","session-id=123");
            usersController.Request.RequestUri = new Uri("http://localhost:50000");
        }

        [TestCleanup]
        public void UserCleanUp()
        {
            DummyDB.CleanDb();
        }

        #endregion

        #region GetAllUsers
        [TestMethod]
        public void GetAllUsersTest()
        {
            Assert.AreEqual(4, usersController.GetAllUsers().Count());
        }
        #endregion

        #region Login
        [TestMethod]
        public void LoginValidTest()
        {
            var users = usersController.GetAllUsers();
            
            var gilUser = users.SingleOrDefault(u => u.name == "גיל");

            Assert.IsNotNull(gilUser);
            Assert.IsNotNull(usersController.Login(gilUser.name, "123"));
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void LoginWrongPassword()
        {
            var users = usersController.GetAllUsers();

            var orUser = users.SingleOrDefault(u => u.name == "אור");

            Assert.IsNotNull(orUser);
            usersController.Login(orUser.name, "123a");
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void LoginWrongUserName()
        {
            var users = usersController.GetAllUsers();

            var orUser = users.SingleOrDefault(u => u.name == "אור");

            Assert.IsNotNull(orUser);
            usersController.Login(orUser.name + "k", "123a");
        }
        #endregion
        
        #region AddUser
        [TestMethod]
        public void AddUserValidTest()
        {
            var users = usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = new User
            {
                id = default(int),
                isAdmin = false,
                name = "gili",
                password = "123"
            };

            usersController.AddUser(user);

            users = usersController.GetAllUsers();
            Assert.IsNotNull(usersController.Login("gili", "123"));
            Assert.AreEqual(5, users.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void AddUserUnauthorized()
        {
            var users = usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = new User
            {
                id = default(int),
                isAdmin = false,
                name = "gili",
                password = "123"
            };

            usersController.Request.Headers.Remove("Cookie");
            usersController.Request.Headers.Add("Cookie", "session-id=1234");
            usersController.AddUser(user);

            users = usersController.GetAllUsers();
            Assert.IsNotNull(usersController.Login("gili", "123"));
            Assert.AreEqual(5, users.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateUserWrongNameWhiteSpaces()
        {
            var users = usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = new User
            {
                id = default(int),
                isAdmin = false,
                name = "     ",
                password = "123123"
            };

            usersController.AddUser(user);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateUserWrongNameEmpty()
        {
            var users = usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = new User
            {
                id = default(int),
                isAdmin = false,
                name = "",
                password = "123123"
            };

            usersController.AddUser(user);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateUserWrongPassWhiteSpaces()
        {
            var users = usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = new User
            {
                id = default(int),
                isAdmin = false,
                name = "גילי",
                password = "       "
            };

            usersController.AddUser(user);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateUserWrongPassEmpty()
        {
            var users = usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = new User
            {
                id = default(int),
                isAdmin = false,
                name = "גילי",
                password = ""
            };

            usersController.AddUser(user);
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateUserAddNameExists()
        {
            var users = usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = new User
            {
                id = default(int),
                isAdmin = false,
                name = "מנהל",
                password = "123"
            };

            usersController.AddUser(user);
        }
        #endregion

        #region UpdateUserName
        [TestMethod]
        public void UpdateUserNameValidInputAdmin()
        {
            var users= usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            usersController.UpdateUserName(2, "גיל המלך");

            users = usersController.GetAllUsers();
            Assert.IsFalse(users.Any(a => a.name == "גיל"));

            var gilUser = users.SingleOrDefault(a => a.name == "גיל המלך");
            Assert.IsNotNull(gilUser);
            Assert.IsNotNull(usersController.Login("גיל המלך", "123"));
            Assert.AreEqual(4, users.Count());
        }

        [TestMethod]
        public void UpdateUserNameValidInputUser()
        {
            var users = usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            usersController.Request.Headers.Remove("Cookie");
            usersController.Request.Headers.Add("Cookie", "session-id=1234");
            usersController.UpdateUserName(1, "אור המלך");
            
            users = usersController.GetAllUsers();
            Assert.IsFalse(users.Any(a => a.name == "אור"));

            var orUser = users.SingleOrDefault(a => a.name == "אור המלך");
            Assert.IsNotNull(orUser);
            Assert.IsNotNull(usersController.Login("אור המלך", "123"));
            Assert.AreEqual(4, users.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateUserNameUnahtorized()
        {
            var users = usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            usersController.Request.Headers.Remove("Cookie");
            usersController.Request.Headers.Add("Cookie", "session-id=1234");
            usersController.UpdateUserName(2, "אור המלך");
            
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateUserNameAlreadyExists()
        {
            usersController.UpdateUserName(2, "אור");
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateUserNameWrongId()
        {
            usersController.UpdateUserName(-2, "גיל המלך");
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateUserNameEmptyName()
        {
            usersController.UpdateUserName(2, "");
        }
        #endregion

        #region UpdateUserPassword
        [TestMethod]
        public void UpdateUserPasswordValidInputAdmin()
        {
            var users = usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = users.Single(u => u.id == 2);
            var userName = user.name;
            var oldPass = user.password;
            
            usersController.UpdateUserPassword(2, "321");

            users = usersController.GetAllUsers();
            Assert.IsFalse(users.Any(u => u.password == oldPass && u.name == userName));
            Assert.IsNotNull(usersController.Login(userName, "321"));
            Assert.AreEqual(4, users.Count());
        }

        [TestMethod]
        public void UpdateUserPasswordValidInputUser()
        {
            var users = usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = users.Single(u => u.id == 1);
            var userName = user.name;
            var oldPass = user.password;

            usersController.Request.Headers.Remove("Cookie");
            usersController.Request.Headers.Add("Cookie", "session-id=1234");
            usersController.UpdateUserPassword(1, "321");

            users = usersController.GetAllUsers();
            Assert.IsFalse(users.Any(u => u.password == oldPass && u.name == userName));
            Assert.IsNotNull(usersController.Login(userName, "321"));
            Assert.AreEqual(4, users.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateUserPasswordValidUnathorized()
        {
            var users = usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = users.Single(u => u.id == 2);
            var userName = user.name;
            var oldPass = user.password;

            usersController.Request.Headers.Remove("Cookie");
            usersController.Request.Headers.Add("Cookie", "session-id=1234");
            usersController.UpdateUserPassword(2, "321");

            users = usersController.GetAllUsers();
            Assert.IsFalse(users.Any(u => u.password == oldPass && u.name == userName));
            Assert.IsNotNull(usersController.Login(userName, "321"));
            Assert.AreEqual(4, users.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateUserPasswordWrongId()
        {
            usersController.UpdateUserName(-2, "321");
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void UpdateUserPasswordEmptyName()
        {
            usersController.UpdateUserName(2, "     ");
        }
        #endregion

        #region DeleteUser
        [TestMethod]
        public void DeleteUserValidInput()
        {
            var users = usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = users.SingleOrDefault(en => en.name == "עובד");
            Assert.IsNotNull(user);

            usersController.DeleteUser((int)user.id);
            users = usersController.GetAllUsers();

            Assert.AreEqual(3, users.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void DeleteUserUnauthorized()
        {
            var users = usersController.GetAllUsers();
            Assert.AreEqual(4, users.Count());

            var user = users.SingleOrDefault(en => en.name == "עובד");
            Assert.IsNotNull(user);

            usersController.Request.Headers.Remove("Cookie");
            usersController.Request.Headers.Add("Cookie", "session-id=1234");
            usersController.DeleteUser((int)user.id);
            users = usersController.GetAllUsers();

            Assert.AreEqual(3, users.Count());
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public void DeleteAnimalIdDoesntExists()
        {
            usersController.DeleteUser(-4);
        }

        #endregion
    }
}
