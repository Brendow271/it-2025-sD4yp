export const setCookie = (name: string, value: string, days: number = 7) =>{
    const date = new Date();
    date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000);
    const expires = `expires=${date.toUTCString()}`;
    document.cookie = `${name}=${encodeURIComponent(value)}; ${expires}; path=/; SameSite=Strict`;
};

export const getCookie = (name: string): string | null => {
    const nameEQ = name + "=";
    const ca = document.cookie.split(';');
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') c = c.substring(1, c.length);
        if (c.indexOf(nameEQ) === 0) return decodeURIComponent(c.substring(nameEQ.length, c.length));
    }
    return null;
};

export const deleteCookie = (name: string) => {
    const paths = ['/', '/pages', '/pages/auth', '/pages/profile'];
    for (const path of paths) {
        document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 GMT; Max-Age=0; path=${path}; SameSite=Strict`;
    }
};

export const authCookies = {
    setAuthToken: (token: string) => setCookie('authToken', token, 7),
    getAuthToken: () => getCookie('authToken'),
    removeAuthToken: () => deleteCookie('authToken'),

    setUserData: (userData: object) => setCookie('userData', JSON.stringify(userData), 7),
    getUserData: () => {
        const userData = getCookie('userData');
        return userData ? JSON.parse(userData) : null;
    },
    removeUserData: () => deleteCookie('userData'),
};