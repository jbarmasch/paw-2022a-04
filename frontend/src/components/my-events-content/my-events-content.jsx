import {useState} from 'react';
import useSwr from 'swr';
import ContentLoading from './content-loading';
import {server} from '../../utils/server';
import MyEventItem from "./my-event-item";

const fetcher = (...args) => fetch(...args).then((res) => res.json())

function Page({index, userId}) {
    const {data, error} = useSwr(`${server}/api/events?page=${index}&userId=${userId.userId}`, fetcher);

    if (error) return <p>No data</p>
    if (!data) return <ContentLoading/>

    return (
        <>
            {data &&
                <section className="products-list">
                    {data.map((item) => (
                        <MyEventItem
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

const MyEventsContent = (userId) => {
    const [orderProductsOpen, setOrderProductsOpen] = useState(false);
    const [pageIndex, setPageIndex] = useState(1);
    console.log(userId)

    return (
        <section className="products-content">
            <div className="products-content__intro">
                <button type="button" onClick={() => setOrderProductsOpen(!orderProductsOpen)}
                        className="products-filter-btn"><i className="icon-filters"></i></button>
                <form className={`products-content__filter ${orderProductsOpen ? 'products-order-open' : ''}`}>
                    <div className="products__filter__select">
                        <h4>Show products: </h4>
                        <div className="select-wrapper">
                            <select>
                                <option>Popular</option>
                            </select>
                        </div>
                    </div>
                    <div className="products__filter__select">
                        <h4>Sort by: </h4>
                        <div className="select-wrapper">
                            <select>
                                <option>Popular</option>
                            </select>
                        </div>
                    </div>
                </form>
            </div>

            <div>
            {/*<div className="event-discovery flex w-full flex-1 flex-col items-center justify-center px-20 text-center">*/}
            <div>
                <Page index={pageIndex} userId={userId}/>
                <div style={{ display: 'none' }}><Page index={pageIndex + 1} userId={userId}/></div>
            </div>
            <button className="pag-button" onClick={() => setPageIndex(pageIndex <= 1 ? pageIndex : pageIndex - 1)}>&laquo;</button>
            <button className="pag-button" onClick={() => setPageIndex(pageIndex + 1)}>&raquo;</button>
        </div>
        </section>
    );
};

export default MyEventsContent
  