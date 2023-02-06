import useSwr from 'swr';
import ProductsLoading from './loading';
import {ProductTypeList} from 'types';
import {server} from '../../../utils/server';
import {useState} from "react";
import MyEventItem from "../../my-event-item";

const fetcher = (...args) => fetch(...args).then((res) => res.json())

type PageType = {
    index?: number;
    userId?: number;
    t
}

function Page({index, userId, t}: PageType) {
    const {data, error} = useSwr(`${server}/api/events?page=${index}&userId=${userId}`, fetcher);

    if (error) return <p>No data</p>
    if (!data) return <ProductsLoading/>

    return (
        <>
            {data &&
                <section className="products-list">
                    {data.map((item: ProductTypeList) => (
                        <MyEventItem
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

const MyEventsContent = ({userId, t}) => {
    const [pageIndex, setPageIndex] = useState(1);

    return (
        <div>
            {/*<div className="event-discovery flex w-full flex-1 flex-col items-center justify-center px-20 text-center">*/}
            <div>
                <Page index={pageIndex} userId={userId} t={t}/>
                <div style={{ display: 'none' }}><Page index={pageIndex + 1} userId={userId} t={t}/></div>
            </div>
            <button className="pag-button" onClick={() => setPageIndex(pageIndex <= 1 ? pageIndex : pageIndex - 1)}>&laquo;</button>
            <button className="pag-button" onClick={() => setPageIndex(pageIndex + 1)}>&raquo;</button>
        </div>
    );
};

export default MyEventsContent