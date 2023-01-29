import {useState} from 'react';
import List from './list';
import { useForm, Controller } from "react-hook-form";

const ProductsContent = () => {
    return (
        <section className="products-content">
            <List/>
        </section>
    );
};

export default ProductsContent
  