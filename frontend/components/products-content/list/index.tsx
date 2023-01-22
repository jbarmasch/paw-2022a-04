import useSwr from 'swr';
import ProductItem from '../../product-item';
import ProductsLoading from './loading';
import {ProductTypeList} from 'types';
import {server} from '../../../utils/server';
import {useEffect, useState} from "react";
import {useRouter} from "next/router";

const fetcher = (...args) => fetch(...args).then((res) => res.json())

function Page({index}) {
    const router = useRouter();

    // useEffect(() => {
    //     if (router.query.search) {
    //         console.log(router.query.search)
    //     }
    // }, [router.query.search])

    let data
    let error

    if (router.query.search) {
        const { data: dataAux, error: errorAux } = useSwr(router.query.search ? `${server}/api/events?page=${index}&search=${router.query.search}` : null, fetcher)
        data = dataAux
        error = errorAux
    } else {
        const { data: dataAux, error: errorAux } = useSwr(`${server}/api/events?page=${index}`, fetcher)
        data = dataAux
        error = errorAux
    }

    if (error) return <p>Loading...</p>
    if (!data) return <p>No data</p>

    return (
        <>
            {!data &&
                <ProductsLoading/>
            }

            {data &&
                <section className="products-list">
                    {data.map((item: ProductTypeList) => (
                        <ProductItem
                            id={item.id}
                            name={item.name}
                            minPrice={item.minPrice}
                            color={item.color}
                            currentPrice={item.minPrice}
                            key={item.id}
                            image={item.image}
                        />
                    ))}
                </section>
            }
        </>
    );
}

const ProductsContent = () => {
    const [pageIndex, setPageIndex] = useState(1);


    return (
        <div>
            <div className="event-discovery flex w-full flex-1 flex-col items-center justify-center px-20 text-center">
                <Page index={pageIndex}/>
                <div style={{ display: 'none' }}><Page index={pageIndex + 1}/></div>
            </div>
            <button className="pag-button" onClick={() => setPageIndex(pageIndex <= 1 ? pageIndex : pageIndex - 1)}>&laquo;</button>
            <button className="pag-button" onClick={() => setPageIndex(pageIndex + 1)}>&raquo;</button>
        </div>
    );
};

export default ProductsContent