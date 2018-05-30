using System;
using System.Collections;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DAL
{
    class Cache
    {
        /// <summary>
        /// The cache dictionary.
        /// </summary>
        private Dictionary<String, object> CacheDict = new Dictionary<string, object>();

        /// <summary>
        /// Sets a new entry in the cache by key and value.
        /// </summary>
        /// <typeparam name="T">The value type.</typeparam>
        /// <param name="value">The value.</param>
        /// <param name="key">The key.</param>
        public void Set<T>(T value, string key) where T : class
        {
            if (CacheDict.ContainsKey(key))
            {
                return;
            }
            else
            {
                CacheDict.Add(key, value);
            }
        }

        /// <summary>
        /// Gets an entry from the cache.
        /// If entry does not exist, fetches it from database and saves it in cache.
        /// </summary>
        /// <typeparam name="T">The value type.</typeparam>
        /// <param name="key">The key.</param>
        /// <returns>The cached data.</returns>
        public T Get<T>(string key) where T : class
        {
            if (!CacheDict.ContainsKey(key))
            {
                return null;
            }
            else
            {
                return CacheDict[key] as T;
            }
        }

        /// <summary>
        /// Removes an entry from the cache.
        /// </summary>
        /// <typeparam name="T">The value type.</typeparam>
        /// <param name="key">The key.</param>
        public void Remove(string key)
        {
            if (CacheDict.ContainsKey(key))
            {
                CacheDict.Remove(key);
            }
        }
    }
}
