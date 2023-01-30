import useSwr from 'swr';
import ProductItem from '../../product-item';
import ProductsLoading from './loading';
import {ProductTypeList} from 'types';
import {server} from '../../../utils/server';
import {useEffect, useState} from "react";
import {useRouter} from "next/router";
import Link from '../../../components/Link'

const fetcher = (...args) => fetch(...args).then((res) => res.json())

function Page({index, t}) {
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

    if (error) return <p>No data</p>
    if (!data) return <ProductsLoading/>

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
                            t={t}
                        />
                    ))}
                </section>
            }
        </>
    );
}

const ProductsContent = ({t}) => {
    const [pageIndex, setPageIndex] = useState(1);
    const [orderProductsOpen, setOrderProductsOpen] = useState(false);
    // const { register, handleSubmit, control, watch, formState: { errors } } = useForm();

    return (
        <>
            <div className="products-content__intro">
                <h2>Men's Tops <span>(133)</span></h2>
                <button type="button" onClick={() => setOrderProductsOpen(!orderProductsOpen)}
                        className="products-filter-btn"><i className="icon-filters"></i></button>
                <form className={`products-content__filter ${orderProductsOpen ? 'products-order-open' : ''}`}>
                    <div className="products__filter__select">
                        <h4>Sort by: </h4>
                        <div className="select-wrapper">
                            <select>
                                <option>{t("filter.username")}</option>
                                <option>{t("filter.rating")}</option>
                            </select>
                        </div>
                    </div>
                    <div className="products__filter__select">
                        <h4>Order: </h4>
                        <div className="select-wrapper">
                            <select>
                                <option>{t("filter.ascending")}</option>
                                <option>{t("filter.descending")}</option>
                            </select>
                        </div>
                    </div>
                </form>
            </div>
        <div>
            <div className="event-discovery flex w-full flex-1 flex-col items-center justify-center px-20 text-center">
                <Page index={pageIndex} t={t}/>
                <div style={{ display: 'none' }}><Page index={pageIndex + 1} t={t}/></div>
            </div>
            <button className="pag-button" onClick={() => setPageIndex(pageIndex <= 1 ? pageIndex : pageIndex - 1)}>&laquo;</button>
            <button className="pag-button" onClick={() => setPageIndex(pageIndex + 1)}>&raquo;</button>
        </div>
        </>

    );
};

export default ProductsContent
