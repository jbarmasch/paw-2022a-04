const dev = process.env.NODE_ENV !== 'production';

// export const server = dev ? 'http://181.46.186.8:2555' : 'http://pawserver.it.itba.edu.ar/paw-2022a-04';
// export const server = 'http://181.46.186.8:2555';
export const server = 'http://pawserver.it.itba.edu.ar/paw-2022a-04';

export const fetcher = async url => {
    const res = await fetch(url)

    if (res.status === 204) {
        return []
    }

    if (!res.ok) {
        const error = new Error('An error occurred while fetching the data.')
        error.info = await res.json()
        error.status = res.status
        throw error
    }

    return res.json()
}