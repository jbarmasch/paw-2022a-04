import {useState} from 'react';
import List from './list';

const MyEventsContent = ({userId}) => {
    const [orderProductsOpen, setOrderProductsOpen] = useState(false);

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

            <List userId={userId}/>
        </section>
    );
};

export default MyEventsContent
  