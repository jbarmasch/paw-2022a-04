import useSwr from 'swr';
import ProductsLoading from './loading';
import {ProductTypeList} from 'types';
import {server} from '../../../utils/server';
import {useState} from "react";
import MyEventItem from "../../my-event-item";

const fetcher = (...args) => fetch(...args).then((res) => res.json())

function Page({index, userId}) {
    const {data, error} = useSwr(`${server}/api/events?page=${index}&userId=${userId}`, fetcher);

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

const MyEventsContent = ({userId}) => {
    const [pageIndex, setPageIndex] = useState(1);

    return (
        <div>
            {/*<div className="event-discovery flex w-full flex-1 flex-col items-center justify-center px-20 text-center">*/}
            <div>
                <Page index={pageIndex} userId={userId}/>
                <div style={{ display: 'none' }}><Page index={pageIndex + 1} userId={userId}/></div>
            </div>
            <button className="pag-button" onClick={() => setPageIndex(pageIndex <= 1 ? pageIndex : pageIndex - 1)}>&laquo;</button>
            <button className="pag-button" onClick={() => setPageIndex(pageIndex + 1)}>&raquo;</button>
        </div>
    );
};

export default MyEventsContent