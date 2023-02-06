import {useState} from 'react';
import List from './list';
import { useForm, Controller } from "react-hook-form";

const ProductsContent = ({t}) => {
    return (
        <section className="products-content">
            <List t={t}/>
        </section>
    );
};

export default ProductsContent
  