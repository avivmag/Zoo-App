using Backend.Models;
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
        [Route("users/all/{language}")]
        public IEnumerable<WorkerUser> GetAllUsers()
        {
            try
            {
                using (var db = GetContext())
                {
                    return db.GetAllUsers();
                }
            }
            catch (ArgumentException argExp)
            {
                //TODO: add log
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
        public WorkerUser GetUserByNameAndPass(string userName, string password)
        {
            try
            {
                using(var db = GetContext())
                {
                    return db.GetUserByNameAndPass(userName, password);
                }
            }
            catch (ArgumentException argExp)
            {
                //TODO: add log
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Adds or Updates the WorkerUser element.
        /// </summary>
        /// <param name="userWorker">The element to add or update</param>
        [HttpPost]
        [Route("users/update")]
        public void UpdateUser(WorkerUser userWorker)
        {
            try
            {
                using (var db = GetContext())
                {
                    db.UpdateUser(userWorker);
                }
            }
            catch (ArgumentException argExp)
            {
                //TODO: add log
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }

        /// <summary>
        /// Deletes the WorkerUSer element.
        /// </summary>
        /// <param name="WorkerUserId">The element's id to delete</param>
        [HttpDelete]
        [Route("users/delete/{workerUserId}")]
        public void DeleteUser(int workerUserId)
        {
            try
            {
                using (var db = GetContext())
                {
                    db.DeleteUser(workerUserId);
                }
            }
            catch
            {
                //TODO: add log
                throw new HttpResponseException(new HttpResponseMessage(HttpStatusCode.InternalServerError));
            }
        }
    }
}