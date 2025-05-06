import { jwtDecode, JwtPayload } from 'jwt-decode';

export const retrieveNonExpiredToken = (): string | null => {
  const token = localStorage.getItem('authToken');
  if (!token) return null;

  try {
    const payload = jwtDecode<JwtPayload>(token);
    return isExpired(payload) ? null : token;
  } catch (error) {
    return null;
  }
};

export const validateToken = async (token: string): Promise<string> => {
  return await fetch(`/api/v1/session/validate`, {
    method: 'POST',
    headers: {
      Authorization: 'Bearer ' + token,
    },
  }).then((response) => {
    if (!response.ok) {
      throw new Error(response.statusText);
    }
    return token;
  });
};

function isExpired(jwt: JwtPayload): boolean {
  if (!jwt || !jwt.exp) {
    return true;
  }
  const now = Math.floor(Date.now() / 1000);
  const diff = jwt.exp - now;
  return diff < 0;
}
