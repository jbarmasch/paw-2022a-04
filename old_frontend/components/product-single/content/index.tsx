import {useState} from 'react';
import productsColors from './../../../utils/data/products-colors';
import productsSizes from './../../../utils/data/products-sizes';
import CheckboxColor from './../../products-filter/form-builder/checkbox-color';
// import {addProduct} from 'store/reducers/cart';
// import {toggleFavProduct} from 'store/reducers/user';
import {ProductTypeList} from 'types';
// import {RootState} from 'store';

type ProductContent = {
    product: ProductTypeList;
}

const Content = ({product}: ProductContent) => {
    const [count, setCount] = useState<number>(1);
    const [color, setColor] = useState<string>('');
    const [itemSize, setItemSize] = useState<string>('');

    const onColorSet = (e: string) => setColor(e);
    const onSelectChange = (e: React.ChangeEvent<HTMLSelectElement>) => setItemSize(e.target.value);


    return (
        <section className="product-content">
            <div className="product-content__intro">
                <h5 className="product__id">Event ID:<br></br>{product.id}</h5>
                <h2 className="product__name">{product.name}</h2>

                <div className="product__prices">
                    <h4>${product.minPrice}</h4>
                </div>
            </div>

            <div className="product-content__filters">
                {/*<div className="product-filter-item">*/}
                {/*    <h5>Color:</h5>*/}
                {/*    <div className="checkbox-color-wrapper">*/}
                {/*        {productsColors.map(type => (*/}
                {/*            <CheckboxColor*/}
                {/*                key={type.id}*/}
                {/*                type={'radio'}*/}
                {/*                name="product-color"*/}
                {/*                color={type.color}*/}
                {/*                valueName={type.label}*/}
                {/*                onChange={onColorSet}*/}
                {/*            />*/}
                {/*        ))}*/}
                {/*    </div>*/}
                {/*</div>*/}
                {/*<div className="product-filter-item">*/}
                {/*    <h5>Size: <strong>See size table</strong></h5>*/}
                {/*    <div className="checkbox-color-wrapper">*/}
                {/*        <div className="select-wrapper">*/}
                {/*            <select onChange={onSelectChange}>*/}
                {/*                <option>Choose size</option>*/}
                {/*                {productsSizes.map(type => (*/}
                {/*                    <option value={type.label}>{type.label}</option>*/}
                {/*                ))}*/}
                {/*            </select>*/}
                {/*        </div>*/}
                {/*    </div>*/}
                {/*</div>*/}
                {/*<div className="product-filter-item">*/}
                {/*    <h5>Quantity:</h5>*/}
                {/*    <div className="quantity-buttons">*/}
                {/*        <div className="quantity-button">*/}
                {/*            <button type="button" onClick={() => setCount(count - 1)} className="quantity-button__btn">*/}
                {/*                -*/}
                {/*            </button>*/}
                {/*            <span>{count}</span>*/}
                {/*            <button type="button" onClick={() => setCount(count + 1)} className="quantity-button__btn">*/}
                {/*                +*/}
                {/*            </button>*/}
                {/*        </div>*/}

                {/*    </div>*/}
                {/*</div>*/}
            </div>
        </section>
    );
};

export default Content;
    