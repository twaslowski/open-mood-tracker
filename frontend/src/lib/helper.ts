export function getFromLocalStorage(key: string): string | null {
  if (typeof window !== 'undefined') {
    return window.localStorage.getItem(key);
  }
  return null;
}

export function storeToken(token: string): void {
  if (typeof window !== 'undefined') {
    window.localStorage.setItem('authToken', token);
  }
}

/**
 * Written as an async function to allow for promise chaining in the calling function
 */
export const getToken = (): string => {
  const token = getFromLocalStorage('authToken');
  if (!token) {
    throw new Error('No token found in local storage');
  }
  return token;
};

export function getFromSessionStorage(key: string): string | null {
  if (typeof sessionStorage !== 'undefined') {
    return sessionStorage.getItem(key);
  }
  return null;
}
