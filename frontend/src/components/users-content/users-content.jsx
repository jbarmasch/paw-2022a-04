import useSwr from 'swr';
import UsersLoading from './content-loading';
import {server} from '../../utils/server';
import UserItem from "./user-item";
import Select from "react-select";
import {useEffect, useState} from "react";

const fetcher = (...args) => fetch(...args).then((res) => res.json())

function Page({index, filters}) {
    let {data, error} = useSwr(`${server}/api/organizers?page=${index}&order=${filters}`, fetcher);

    if (error) return <p>No data</p>
    if (!data) return <UsersLoading/>

    return (
        <>
            {data &&
                <section className="user-list">
                    {data.map((item) => (
                        <UserItem
                            key={item.id}
                            id={item.id}
                            username={item.username}
                            rating={item.rating}
                            votes={item.votes}
                        />
                    ))}
                </section>
            }
        </>
    );
}

const UsersContent = () => {
    const [pageIndex, setPageIndex] = useState(1)
    const [orderProductsOpen, setOrderProductsOpen] = useState(false);
    const [filter, setFilter] = useState("")

    const handleLocationChange = (event) => {
        setFilter(event.value);
    };

    const style = {
        control: base => ({
            ...base,
            caretColor: "transparent",
            border: "revert",
            background: "transparent",
            boxShadow: "1px 1px transparent",
            height: "42px"
        })
    }

    let tagList = []
    tagList.push({
        value: "USERNAME_ASC",
        label: "Username ascendente pai"
    })
    tagList.push({
        value: "USERNAME_DESC",
        label: "Username descendente pai"
    })

    return (
        <section className="products-content">
            <div className="products-content__intro">
                <button type="button" onClick={() => setOrderProductsOpen(!orderProductsOpen)}
                        className="products-filter-btn"><i className="icon-filters"></i></button>
                <form className={`products-content__filter ${orderProductsOpen ? 'products-order-open' : ''}`}>
                    <div className="products__filter__select">
                        <h4>Show products: </h4>
                            <Select
                                id="type-input"
                                instanceId="type-input"
                                name="type"
                                className="form__input"
                                classNamePrefix="select"
                                options={tagList}
                                styles={style}
                                onChange={handleLocationChange}
                                placeholder="Select type"/>
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
                <div>
                    <Page index={pageIndex} filters={filter}/>
                    <div style={{ display: 'none' }}><Page index={pageIndex + 1} filters={filter}/></div>
                </div>
                <button className="pag-button" onClick={() => setPageIndex(pageIndex <= 1 ? pageIndex : pageIndex - 1)}>&laquo;</button>
                <button className="pag-button" onClick={() => setPageIndex(pageIndex + 1)}>&raquo;</button>
            </div>
        </section>
    );
};

export default UsersContent
  