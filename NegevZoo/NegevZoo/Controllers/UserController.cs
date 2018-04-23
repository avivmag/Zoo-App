using DAL;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace NegevZoo.Controllers
{
    public class UserController : ControllerBase
    {
        /// <summary>
        /// Gets all the Users.
        /// </summary>
        /// <returns>All the Users.</returns>
        [HttpGet]
        [Route("users/all")]
        public IEnumerable<User> GetAllUsers()
        {
            try
            {
                using (var db = GetContext())
                {
                    return db.GetAllUsers();
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance().WriteLine(Exp.Message, Exp.StackTrace);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Connect a worker user to the system.
        /// </summary>
        /// <param name="userName">The wanted user name</param>
        /// <param name="password">The user's password</param>
        /// <returns>a boolean that indicates if the proccess succeded.</returns>
        [HttpGet]
        [Route("users/login/{userName}/{password}")]
        public bool Login(string userName, string password)
        {
            try
            {
                using (var db = GetContext())
                {
                    return db.Login(userName, password);
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance().WriteLine(Exp.Message, Exp.StackTrace);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }
        
        /// <summary>
        /// Gets User by user name and password.
        /// </summary>
        /// <param name="userName">The wanted user name</param>
        /// <param name="password">The user's password</param>
        /// <returns>The user that correspond to the user name and password.</returns>
        [HttpGet]
        [Route("users/nameAndPass/{userName}/{password}")]
        public User GetUserByNameAndPass(string userName, string password)
        {
            try
            {
                using(var db = GetContext())
                {
                    return db.GetUserByNameAndPass(userName, password);
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance().WriteLine(Exp.Message, Exp.StackTrace);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Adds or Updates the User element.
        /// </summary>
        /// <param name="userWorker">The element to add or update</param>
        [HttpPost]
        [Route("users/update")]
        public void UpdateUser(User userWorker)
        {
            try
            {
                using (var db = GetContext())
                {
                    db.UpdateUser(userWorker);
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance().WriteLine(Exp.Message, Exp.StackTrace);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Deletes the User element.
        /// </summary>
        /// <param name="UserId">The element's id to delete</param>
        [HttpDelete]
        [Route("users/delete/{UserId}")]
        public void DeleteUser(int UserId)
        {
            try
            {
                using (var db = GetContext())
                {
                    db.DeleteUser(UserId);
                }
            }
            catch (Exception Exp)
            {
                Logger.GetInstance().WriteLine(Exp.Message, Exp.StackTrace);
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }
    }
}