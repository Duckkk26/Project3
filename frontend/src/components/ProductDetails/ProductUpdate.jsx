import React, { useEffect, useRef, useState } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import cn from 'classnames';

// Bootstrap
import { Collapse, UncontrolledTooltip } from 'reactstrap'

// CKEditor
import { CKEditor } from '@ckeditor/ckeditor5-react';
import {
    ClassicEditor,
    AccessibilityHelp,
    Autoformat,
    AutoImage,
    Autosave,
    BalloonToolbar,
    BlockQuote,
    Bold,
    Essentials,
    FullPage,
    GeneralHtmlSupport,
    Heading,
    HtmlComment,
    HtmlEmbed,
    ImageBlock,
    ImageCaption,
    ImageInline,
    ImageInsert,
    ImageInsertViaUrl,
    ImageResize,
    ImageStyle,
    ImageTextAlternative,
    ImageToolbar,
    ImageUpload,
    Indent,
    IndentBlock,
    Italic,
    Link as LinkURL,
    LinkImage,
    List,
    ListProperties,
    MediaEmbed,
    Paragraph,
    PasteFromOffice,
    PictureEditing,
    SelectAll,
    ShowBlocks,
    SourceEditing,
    Table,
    TableCaption,
    TableCellProperties,
    TableColumnResize,
    TableProperties,
    TableToolbar,
    TextTransformation,
    TodoList,
    Underline,
    Undo
} from 'ckeditor5';
import 'ckeditor5/ckeditor5.css';

import { DragDropContext, Draggable, Droppable } from 'react-beautiful-dnd';

// Import Icons
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCaretDown, faChevronLeft, faPlus, faPlusCircle, faXmarkCircle } from '@fortawesome/free-solid-svg-icons'
import infoIcon from '../../assets/icons/InfoIcon'
import TypeItem from '../TypeItem/TypeItem';
import { uploadImage } from '../../service/UploadAPI';
import { createProduct, updateProduct } from '../../service/ProductAAPI';
import ListSelectPopup from '../ListSelectPopup/ListSelectPopup';
import { getListCategory } from '../../service/CategoryAPI';
import { getListBrand } from '../../service/BrandAPI';
import { getProductById } from '../../service/ProductAPI';
import { toast } from 'react-toastify';
import Notification from '../Notification/Notification';
import { withAuthorization } from '../../hoc';

const ProductUpdate = () => {
    const navigate = useNavigate();
    const { productId } = useParams();

    const [dataBody, setDataBody] = useState({
        sub_id: null,
        name: null,
        unit: null,
        cost_price: null,
        wholesale_price: null,
        retail_price: null,
        images: [],
        types: [],
        category_id: null,
        brand_id: null,
        tags: null,
        description: null,
        status: "ACTIVE"
    });

    const handleUpdateProduct = async () => {
        let updatedDataBody = { ...dataBody };

        if (images.length > 0) {
            // Tải tất cả hình ảnh và chờ kết quả
            const uploadedImages = await Promise.all(
                images.map(async (image) => {
                    // Await each image upload
                    const response = await uploadImage(image.src);

                    // Return the formatted image object (URL and alt text)
                    return {
                        url: response,
                        alt: image.src.name || 'Image' // Ensure there's a fallback alt text
                    };
                })
            );

            // Update dataBody with the newly uploaded images
            updatedDataBody = {
                ...updatedDataBody,
                images: uploadedImages
            };
        }

        const response = await updateProduct(productId, updatedDataBody);
        if (response.status_code === 200) {
            toast(<Notification 
                    type={"success"} 
                    withIcon 
                    message={"Cập nhật sản phẩm thành công!"} 
                />,
                {
                    autoClose: 4000,
                    closeButton: false,
                    hideProgressBar: true,
                }
            )
            navigate('/admin/products/PRD/' + productId);
        }
    };

    const [isOpenEditor, setIsOpenEditor] = useState(false)

    const editorConfig = {
        language: 'vi',
        toolbar: {
            items: [
                'undo',
                'redo',
                '|',
                'sourceEditing',
                'showBlocks',
                '|',
                'heading',
                '|',
                'bold',
                'italic',
                'underline',
                '|',
                'link',
                'insertImage',
                'mediaEmbed',
                'insertTable',
                'blockQuote',
                'htmlEmbed',
                '|',
                'bulletedList',
                'numberedList',
                'todoList',
                'outdent',
                'indent'
            ],
            shouldNotGroupWhenFull: false
        },
        plugins: [
            AccessibilityHelp,
            Autoformat,
            AutoImage,
            Autosave,
            BalloonToolbar,
            BlockQuote,
            Bold,
            Essentials,
            FullPage,
            GeneralHtmlSupport,
            Heading,
            HtmlComment,
            HtmlEmbed,
            ImageBlock,
            ImageCaption,
            ImageInline,
            ImageInsert,
            ImageInsertViaUrl,
            ImageResize,
            ImageStyle,
            ImageTextAlternative,
            ImageToolbar,
            ImageUpload,
            Indent,
            IndentBlock,
            Italic,
            LinkURL,
            LinkImage,
            List,
            ListProperties,
            MediaEmbed,
            Paragraph,
            PasteFromOffice,
            PictureEditing,
            SelectAll,
            ShowBlocks,
            SourceEditing,
            Table,
            TableCaption,
            TableCellProperties,
            TableColumnResize,
            TableProperties,
            TableToolbar,
            TextTransformation,
            TodoList,
            Underline,
            Undo
        ],
        balloonToolbar: ['bold', 'italic', '|', 'link', 'insertImage', '|', 'bulletedList', 'numberedList'],
        heading: {
            options: [
                {
                    model: 'paragraph',
                    title: 'Paragraph',
                    class: 'ck-heading_paragraph'
                },
                {
                    model: 'heading1',
                    view: 'h1',
                    title: 'Heading 1',
                    class: 'ck-heading_heading1'
                },
                {
                    model: 'heading2',
                    view: 'h2',
                    title: 'Heading 2',
                    class: 'ck-heading_heading2'
                },
                {
                    model: 'heading3',
                    view: 'h3',
                    title: 'Heading 3',
                    class: 'ck-heading_heading3'
                },
                {
                    model: 'heading4',
                    view: 'h4',
                    title: 'Heading 4',
                    class: 'ck-heading_heading4'
                },
                {
                    model: 'heading5',
                    view: 'h5',
                    title: 'Heading 5',
                    class: 'ck-heading_heading5'
                },
                {
                    model: 'heading6',
                    view: 'h6',
                    title: 'Heading 6',
                    class: 'ck-heading_heading6'
                }
            ]
        },
        htmlSupport: {
            allow: [
                {
                    name: /^.*$/,
                    styles: true,
                    attributes: true,
                    classes: true
                }
            ]
        },
        image: {
            toolbar: [
                'toggleImageCaption',
                'imageTextAlternative',
                '|',
                'imageStyle:inline',
                'imageStyle:wrapText',
                'imageStyle:breakText',
                '|',
                'resizeImage',
                '|',
                'ckboxImageEdit'
            ]
        },
        link: {
            addTargetToExternalLinks: true,
            defaultProtocol: 'https://',
            decorators: {
                toggleDownloadable: {
                    mode: 'manual',
                    label: 'Downloadable',
                    attributes: {
                        download: 'file'
                    }
                }
            }
        },
        list: {
            properties: {
                styles: true,
                startIndex: true,
                reversed: true
            }
        },
        table: {
            contentToolbar: ['tableColumn', 'tableRow', 'mergeTableCells', 'tableProperties', 'tableCellProperties']
        }
    };

    const [images, setImages] = useState([]);
    const imagesInputRef = useRef(null);

    const handleImageInputClick = () => {
        imagesInputRef.current.click();
    }

    const handleImages = (e) => {
        const files = e.target.files || e.dataTransfer.files;

        const fileArray = Array.from(files);
        const newImages = fileArray.map((file, index) => ({
            id: images.length + index,
            src: file
        }));

        setImages(prevImages => [...prevImages, ...newImages]);
    };

    const deleteImage = (image) => {
        setImages(prev => prev.filter((value) => value !== image))
    }

    const handleDragOver = (e) => {
        e.preventDefault();
    }

    const handleDrop = (e) => {
        e.preventDefault();
        handleImages(e);
    };

    const handleOnDragEnd = (result) => {
        const { destination, source } = result;

        // Nếu không có đích đến hoặc vị trí không thay đổi, không cần làm gì
        if (!destination || destination.index === source.index) {
            return;
        }

        // Tạo bản sao của danh sách ảnh
        const reorderedImages = Array.from(images);

        // Cắt ảnh tại vị trí nguồn (nơi kéo đi) và chèn vào vị trí đích
        const [movedImage] = reorderedImages.splice(source.index, 1);
        reorderedImages.splice(destination.index, 0, movedImage);

        // Cập nhật danh sách ảnh với vị trí mới
        setImages(reorderedImages);
    };

    const [isTypes, setIsTypes] = useState(false);
    const [types, setTypes] = useState([]);

    useEffect(() => {
        if (isTypes) {
            setTypes(prev => [...prev, {
                name: "",
                value: ""
            }])
        } else {
            setTypes([]);
        }
    }, [isTypes])

    const addType = () => {
        setTypes([...types, {
            name: "",
            value: ""
        }])
    }

    const deleteType = (index) => {
        setTypes(prev => prev.filter((_, i) => i !== index));
    }

    const updateType = (index, name, value) => {
        const updatedTypes = [...types];
        updatedTypes[index] = { ...updatedTypes[index], [name]: value }

        setDataBody(prevState => {
            return {
                ...prevState,
                types: updatedTypes
            }
        });
        setTypes(updatedTypes);
    }

    const [categoryList, setCategoryList] = useState([]);
    const [category, setCategory] = useState(null);
    const [isCategoryPopup, setIsCategoryPopup] = useState(false);
    const categoryBtnRef = useRef(null);

    const [categoryDataFilter, setCategoryDataFilter] = useState({
        keyword: null
    });
    const [dataPageCategory, setDataPageCategory] = useState({
        currentPage: 1,
        totalPage: 1,
        currentSize: 10,
    });

    const fetchCategoryList = async () => {
        const response = await getListCategory(dataPageCategory.currentPage, dataPageCategory.currentSize, categoryDataFilter);
        setCategoryList(response.data.data);
        setDataPageCategory(prevState => {
            return {
                ...prevState,
                totalPage: response.data.total_page
            }
        });
    }
    const fetchMoreCategory = async () => {
        if (dataPageCategory.currentPage < dataPageCategory.totalPage) {
            const response = await getListCategory(dataPageCategory.currentPage + 1, dataPageCategory.currentSize, categoryDataFilter);
            setCategoryList(prevList => [...prevList, ...response.data.data]);
            setDataPageCategory(prevState => {
                return {
                    ...prevState,
                    currentPage: prevState.currentPage + 1,
                    totalPage: response.data.total_page
                }
            });
        }
    }

    const handleFetchMoreCategory = () => {
        if (isCategoryPopup) {
            fetchCategoryList();
        } else {
            setCategoryList([]);
            setCategoryDataFilter((prev) => {
                return (
                    {
                        ...prev,
                        keyword: ""
                    }
                )
            })
            setDataPageCategory(prevState => {
                return {
                    ...prevState,
                    currentPage: 1,
                    totalPage: 1
                }
            });
        }
    }
    useEffect(() => {
        handleFetchMoreCategory();
    }, [isCategoryPopup])
    useEffect(() => {
        setDataPageCategory(prevState => {
            return {
                ...prevState,
                currentPage: 1
            }
        });
        handleFetchMoreCategory();
    }, [categoryDataFilter.keyword])

    useEffect(() => {
        if (categoryList.length > 0) {
            const selectedCategory = categoryList.find(category => category.id === dataBody.category_id);

            setCategory(selectedCategory);
        }
    }, [dataBody.category_id])

    const [brandList, setBrandList] = useState([]);
    const [brand, setBrand] = useState({});
    const [isBrandPopup, setIsBrandPopup] = useState(false);
    const brandBtnRef = useRef(null);
    const [brandDataFilter, setBrandDataFilter] = useState({
        name: null
    });
    const [dataPageBrand, setDataPageBrand] = useState({
        currentPage: 1,
        totalPage: 1,
        currentSize: 10,
    });

    const fetchBrandList = async () => {
        const response = await getListBrand(dataPageBrand.currentPage, dataPageBrand.currentSize, brandDataFilter);
        setBrandList(response.data.data);
        setDataPageBrand(prevState => {
            return {
                ...prevState,
                totalPage: response.data.total_page
            }
        });
    }
    const fetchMoreBrand = async () => {
        if (dataPageBrand.currentPage < dataPageBrand.totalPage) {
            const response = await getListBrand(dataPageBrand.currentPage + 1, dataPageBrand.currentSize, brandDataFilter);
            setBrandList(prevList => [...prevList, ...response.data.data]);
            setDataPageBrand(prevState => {
                return {
                    ...prevState,
                    currentPage: prevState.currentPage + 1,
                    totalPage: response.data.total_page
                }
            });
        }
    }

    const handleFetchMoreBrand = () => {
        if (isBrandPopup) {
            fetchBrandList();
        } else {
            setBrandList([]);
            setBrandDataFilter((prev) => {
                return (
                    {
                        ...prev,
                        name: ""
                    }
                )
            })
            setDataPageBrand(prevState => {
                return {
                    ...prevState,
                    currentPage: 1,
                    totalPage: 1
                }
            });
        }
    }
    useEffect(() => {
        handleFetchMoreBrand();
    }, [isBrandPopup])
    useEffect(() => {
        setDataPageBrand(prevState => {
            return {
                ...prevState,
                currentPage: 1
            }
        });
        handleFetchMoreBrand();
    }, [brandDataFilter.name])
    useEffect(() => {
        if (brandList.length > 0) {
            const selectedBrand = brandList.find(brand => brand.id === dataBody.brand_id);

            setBrand(selectedBrand);
        }
    }, [dataBody.brand_id])

    useEffect(() => {
        const fetchProductDetails = async () => {
            if (productId) {
                const responseAPI = await getProductById(productId);
                setDataBody({
                    sub_id: responseAPI.data.sub_id,
                    name: responseAPI.data.name,
                    unit: responseAPI.data.unit,
                    cost_price: responseAPI.data.cost_price,
                    wholesale_price: responseAPI.data.wholesale_price,
                    retail_price: responseAPI.data.retail_price,
                    images: [responseAPI.data.image] || [],
                    types: responseAPI.data.types,
                    category_id: responseAPI.data.category_id,
                    brand_id: responseAPI.data.brand_id,
                    tags: responseAPI.data.tags,
                    description: responseAPI.data.description,
                    status: responseAPI.data.status,
                });

                setCategory({
                    created_at: null,
                    updated_at: null,
                    id: responseAPI.data?.category_id,
                    sub_id: null,
                    name: responseAPI.data?.category_name || '',
                });
                setBrand({
                    id: responseAPI.data?.brand_id,
                    name: responseAPI.data?.brand_name || '',
                    sub_id: null,
                });

                const fetchAndSetImage = async () => {
                    const response = await fetch(responseAPI.data?.image?.url);
                    const blob = await response.blob();
                    // Tạo File từ Blob
                    const file = new File([blob], `image-${Date.now()}.jpg`, { type: blob.type });
                    
                    // Cập nhật files vào state
                    setImages(prev => [...prev, {
                        id: images.length + 1, // Đảm bảo id là duy nhất cho mỗi ảnh
                        src: file
                    }]);
                };

                fetchAndSetImage();

                if (responseAPI.data?.types.length > 0) {
                    setIsTypes(true);
                    setTypes(responseAPI.data.types);
                }
            }
        };

        fetchProductDetails();
    }, []);

    return (
        <>
            <div className="right__navbar">
                <div className="box-navbar">
                    <div className="btn-toolbar">
                        <Link to='/admin/products' className='btn-back'>
                            <FontAwesomeIcon icon={faChevronLeft} />
                            <h6 className="btn-back__title">
                                Quay lại danh sách sản phẩm
                            </h6>
                        </Link>
                    </div>
                    <div className="btn-toolbar">
                        <button className="btn btn-outline-primary" onClick={() => navigate(`/admin/products/PRD/${productId}`)}>
                            <span className="btn__title">Thoát</span>
                        </button>
                        <button className="btn btn-primary" onClick={handleUpdateProduct}>
                            <span className="btn__title">Lưu</span>
                        </button>
                    </div>
                </div>
            </div>
            <div className="right__createObjectPage">
                <div className="right__createObjectPage-wrapper">
                    <div className="right__createObjectPage-container">
                        <div className="box-maininfo">
                            <div className="box-info-item box-general">
                                <div className="info-header">
                                    <div className="box-header">
                                        <h6>Thông tin chung</h6>
                                    </div>
                                </div>
                                <div className="info-content">
                                    <div className="grid-container">
                                        <div className="box-product-name">
                                            <div className="form-item">
                                                <label htmlFor="name" className="form-label">
                                                    Tên sản phẩm
                                                    <span
                                                        id='nameCaption'
                                                        className="caption-icon"
                                                    >
                                                        {infoIcon}
                                                    </span>
                                                    <UncontrolledTooltip
                                                        placement="top"
                                                        target="nameCaption"
                                                    >
                                                        Tên sản phẩm không bao gồm các giá trị thuộc tính như màu sắc, chất liệu, kích cỡ...
                                                    </UncontrolledTooltip>
                                                    <span className="asterisk-icon">*</span>
                                                </label>
                                                <div className="form-textfield">
                                                    <input
                                                        value={dataBody.name}
                                                        type="text"
                                                        name="name"
                                                        id="name"
                                                        placeholder='Nhập tên sản phẩm'
                                                        onChange={e => setDataBody(prevState => ({
                                                            ...prevState,
                                                            name: e.target.value
                                                        }))}
                                                    />
                                                    <fieldset className="input-field"></fieldset>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="box-product-id">
                                            <div className="form-item">
                                                <label htmlFor="id" className="form-label">
                                                    Mã sản phẩm
                                                    <span
                                                        id='idCaption'
                                                        className="caption-icon"
                                                    >
                                                        {infoIcon}
                                                    </span>
                                                    <UncontrolledTooltip
                                                        placement="top"
                                                        target="idCaption"
                                                    >
                                                        Mã <strong>không trùng lặp</strong> để định danh giữa các sản phẩm.<br />
                                                        Nếu để trống trường này, mã sản phẩm sẽ được tự sinh với <strong>tiền tố PVN</strong>
                                                    </UncontrolledTooltip>
                                                </label>
                                                <div className="form-textfield">
                                                    <input
                                                        value={dataBody.sub_id}
                                                        type="text"
                                                        name="id"
                                                        id="id"
                                                        disabled={true}
                                                        onChange={e => setDataBody(prevState => ({
                                                            ...prevState,
                                                            sub_id: e.target.value
                                                        }))} />
                                                    <fieldset className="input-field"></fieldset>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="box-product-unit">
                                            <div className="form-item">
                                                <label htmlFor="unit" className="form-label">
                                                    Đơn vị tính
                                                </label>
                                                <div className="form-textfield">
                                                    <input
                                                        value={dataBody.unit}
                                                        type="text"
                                                        name="unit"
                                                        id="unit"
                                                        onChange={e => setDataBody(prevState => {
                                                            return {
                                                                ...prevState,
                                                                unit: e.target.value
                                                            }
                                                        })} />
                                                    <fieldset className="input-field"></fieldset>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="box-product-description">
                                            <button onClick={() => setIsOpenEditor(!isOpenEditor)} className="btn-base btn-add-description">
                                                <span className='btn__label'>
                                                    <p>{isOpenEditor ? "Ẩn mô tả sản phẩm" : "Mô tả sản phẩm"}</p>
                                                </span>
                                            </button>
                                            <Collapse className='box-description' isOpen={isOpenEditor}>
                                                <div className="box-description__container">
                                                    <CKEditor
                                                        editor={ClassicEditor}
                                                        config={editorConfig}
                                                        onChange={(event, editor) => {
                                                            const data = editor.getData();
                                                            setDataBody(prevState => {
                                                                return {
                                                                    ...prevState,
                                                                    description: data
                                                                }
                                                            })
                                                        }}
                                                    />
                                                </div>
                                            </Collapse>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="box-info-item box-price">
                                <div className="info-header">
                                    <div className="box-header">
                                        <h6>Giá sản phẩm</h6>
                                    </div>
                                </div>
                                <div className="info-content">
                                    <div className="grid-container">
                                        <div className="box-retail-price">
                                            <div className="form-item">
                                                <label htmlFor="retail-price" className="form-label">
                                                    Giá bán lẻ
                                                    <span
                                                        id='retailPriceCaption'
                                                        className="caption-icon"
                                                    >
                                                        {infoIcon}
                                                    </span>
                                                    <UncontrolledTooltip
                                                        placement="top"
                                                        target="retailPriceCaption"
                                                    >
                                                        Giá bán cho các khách hàng mua lẻ
                                                    </UncontrolledTooltip>
                                                </label>
                                                <div className="form-textfield">
                                                    <input
                                                        value={dataBody.retail_price}
                                                        className='text-end'
                                                        type="text"
                                                        name="retail-price"
                                                        id="retail-price"
                                                        onChange={e => setDataBody(prevState => {
                                                            return {
                                                                ...prevState,
                                                                retail_price: e.target.value
                                                            }
                                                        })} />
                                                    <fieldset className="input-field"></fieldset>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="box-wholesale-price">
                                            <div className="form-item">
                                                <label htmlFor="wholesale-price" className="form-label">
                                                    Giá bán buôn
                                                    <span
                                                        id='wholesalePriceCaption'
                                                        className="caption-icon"
                                                    >
                                                        {infoIcon}
                                                    </span>
                                                    <UncontrolledTooltip
                                                        placement="top"
                                                        target="wholesalePriceCaption"
                                                    >
                                                        Giá bán cho các khách hàng mua với số lượng lớn
                                                    </UncontrolledTooltip>
                                                </label>
                                                <div className="form-textfield">
                                                    <input
                                                        value={dataBody.wholesale_price}
                                                        className='text-end'
                                                        type="text"
                                                        name="wholesale-price"
                                                        id="wholesale-price"
                                                        onChange={e => setDataBody(prevState => {
                                                            return {
                                                                ...prevState,
                                                                wholesale_price: e.target.value
                                                            }
                                                        })}
                                                    />
                                                    <fieldset className="input-field"></fieldset>
                                                </div>
                                            </div>
                                        </div>
                                        <hr />
                                        <div className="box-cost-price">
                                            <div className="form-item">
                                                <label htmlFor="cost-price" className="form-label">
                                                    Giá nhập
                                                    <span
                                                        id='costPriceCaption'
                                                        className="caption-icon"
                                                    >
                                                        {infoIcon}
                                                    </span>
                                                    <UncontrolledTooltip
                                                        placement="top"
                                                        target="costPriceCaption"
                                                    >
                                                        Giá <strong>tự động gợi ý</strong> khi bạn <strong>tạo đơn nhập hàng</strong> từ nhà cung cấp. Bạn có thể thay đổi tùy theo giá nhập hàng thực tế
                                                    </UncontrolledTooltip>
                                                </label>
                                                <div className="form-textfield">
                                                    <input
                                                        value={dataBody.cost_price}
                                                        className='text-end'
                                                        type="text"
                                                        name="cost-price"
                                                        id="cost-price"
                                                        onChange={e => setDataBody(prevState => {
                                                            return {
                                                                ...prevState,
                                                                cost_price: e.target.value
                                                            }
                                                        })}
                                                    />
                                                    <fieldset className="input-field"></fieldset>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="box-info-item box-images">
                                <div className="info-header">
                                    <div className="box-header">
                                        <h6>Ảnh sản phẩm {`${images.length > 0 ? `(${images.length})` : ''}`}</h6>
                                    </div>
                                    {images.length > 0 && <div onClick={() => setImages([])} className="btn-delete-all">
                                        <p>Xóa tất cả</p>
                                    </div>}
                                </div>
                                <div className="info-content">
                                    {images.length > 0 && <DragDropContext onDragEnd={handleOnDragEnd}>
                                        <Droppable droppableId='images' direction='horizontal'>
                                            {(provided) => (
                                                <div
                                                    {...provided.droppableProps}
                                                    ref={provided.innerRef}
                                                    onDragOver={handleDragOver}
                                                    onDrop={handleDrop}
                                                    className="box-show-images"
                                                >
                                                    <div id='uploadArea' onClick={handleImageInputClick} className="upload-area">
                                                        <FontAwesomeIcon icon={faPlus} />
                                                    </div>
                                                    <UncontrolledTooltip
                                                        placement="bottom"
                                                        target="uploadArea"
                                                    >
                                                        Kéo thả hoặc tải ảnh
                                                    </UncontrolledTooltip>
                                                    {}
                                                    {images?.map((image, index) => (
                                                        <Draggable key={image.id} draggableId={image.id.toString()} index={index}>
                                                            {(provided) => (
                                                                <div
                                                                    className="image-item"
                                                                    ref={provided.innerRef}
                                                                    {...provided.draggableProps}
                                                                    {...provided.dragHandleProps}
                                                                >
                                                                    <span onClick={() => deleteImage(image)} className="delete-icon">
                                                                        <FontAwesomeIcon icon={faXmarkCircle} />
                                                                    </span>
                                                                    <div className="box-image">
                                                                        <img src={URL.createObjectURL(image.src)} alt={`Preview ${index}`} />
                                                                    </div>
                                                                </div>
                                                            )}
                                                        </Draggable>
                                                    ))}
                                                    {provided.placeholder}
                                                </div>
                                            )}
                                        </Droppable>
                                    </DragDropContext>}
                                    <div
                                        onClick={handleImageInputClick}
                                        onDragOver={handleDragOver}
                                        onDrop={handleDrop}
                                        className={cn("box-add-images", { "d-none": images.length > 0 })}
                                    >
                                        <FontAwesomeIcon icon={faPlus} />
                                        <p>
                                            Kéo thả hoặc&nbsp;
                                            <a>tải ảnh lên từ thiết bị</a>
                                        </p>
                                        <div className="images-input">
                                            <input
                                                ref={imagesInputRef}
                                                onChange={(e) => handleImages(e)}
                                                aria-invalid="false"
                                                autoComplete='off'
                                                type="file"
                                                accept='image/gif, image/jpeg, image/jpg, image/png, image/bmp'
                                                multiple
                                                tabIndex={-1}
                                                name="images"
                                                id="images-input"
                                            />
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="box-info-item box-types">
                                <div className="info-header">
                                    <div className="box-header">
                                        <h6>Thuộc tính</h6>
                                        <span
                                            id='typesCaption'
                                            className="caption-icon"
                                        >
                                            {infoIcon}
                                        </span>
                                        <UncontrolledTooltip
                                            placement="top"
                                            target="typesCaption"
                                        >
                                            Tạo các thuộc tính để phân biệt các mẫu khác nhau của sản phẩm <br />
                                            <strong>Ví dụ:</strong> Kích thước, Màu sắc, Chất liệu,...
                                        </UncontrolledTooltip>
                                        <div className="box-switch">
                                            <div className="btn-switch">
                                                <input onChange={() => setIsTypes(!isTypes)} value={isTypes} type="checkbox" name="switch" className='switch-checkbox' id="" />
                                                <span className="switch-bar"></span>
                                            </div>
                                        </div>
                                    </div>
                                    <div className="box-subtitle">
                                        <p>Thêm mới thuộc tính giúp sản phẩm có nhiều lựa chọn, như kích cỡ hay màu sắc</p>
                                    </div>
                                </div>
                                <Collapse className='info-panel' isOpen={isTypes}>
                                    <div className="info-content">
                                        <div className="grid-container types-header">
                                            <h6 className="box-type-name">Tên thuộc tính</h6>
                                            <h6 className="box-type-value">Giá trị</h6>
                                        </div>
                                        {
                                            types?.map((type, index) => (
                                                <TypeItem
                                                    key={index}
                                                    index={index}
                                                    type={type}
                                                    handleUpdateType={(name, value) => updateType(index, name, value)}
                                                    handleDeleteType={() => deleteType(index)}
                                                />
                                            ))
                                        }
                                        <div onClick={addType} className="type-item btn-add-type">
                                            <FontAwesomeIcon icon={faPlusCircle} />
                                            <p>Thêm thuộc tính khác</p>
                                        </div>
                                    </div>
                                </Collapse>
                            </div>
                        </div>
                        <div className="box-subinfo">
                            <div className="box-info-item">
                                <div className="info-header">
                                    <div className="box-header">
                                        <h6>Thông tin bổ sung</h6>
                                    </div>
                                </div>
                                <div className="info-content">
                                    <div className="form-item">
                                        <label htmlFor="category" className="form-label">
                                            Loại sản phẩm
                                        </label>
                                        <div className="box-select">
                                            <button ref={categoryBtnRef} onClick={() => setIsCategoryPopup(!isCategoryPopup)} id='category' className="btn-select">
                                                {category?.name || 'Chọn loại sản phẩm'}
                                                <FontAwesomeIcon icon={faCaretDown} />
                                            </button>
                                            {
                                                isCategoryPopup &&
                                                <ListSelectPopup
                                                    title={"loại sản phẩm"}
                                                    isLarge={false}
                                                    isSearch={true}
                                                    keyword={categoryDataFilter.keyword}
                                                    handleChangeKeyword={(e) => {
                                                        setCategoryDataFilter({
                                                            ...categoryDataFilter,
                                                            keyword: e.target.value
                                                        })
                                                    }}
                                                    isFastCreate={true}
                                                    dataList={categoryList}
                                                    handleSelect={(id) => setDataBody({
                                                        ...dataBody,
                                                        category_id: id
                                                    })}
                                                    btnRef={categoryBtnRef}
                                                    closePopup={() => setIsCategoryPopup(false)}
                                                    fetchMoreData={fetchMoreCategory}
                                                />
                                            }
                                        </div>
                                    </div>
                                    <div className="form-item">
                                        <label htmlFor="brand" className="form-label">
                                            Nhãn hiệu
                                        </label>
                                        <div className="box-select">
                                            <button ref={brandBtnRef} onClick={() => setIsBrandPopup(!isBrandPopup)} id='brand' className="btn-select">
                                                {brand?.name || 'Chọn nhãn hiệu'}
                                                <FontAwesomeIcon icon={faCaretDown} />
                                            </button>
                                            {
                                                isBrandPopup &&
                                                <ListSelectPopup
                                                    title={"nhãn hiệu"}
                                                    isLarge={false}
                                                    isSearch={true}
                                                    keyword={brandDataFilter.name}
                                                    handleChangeKeyword={(e) => {
                                                        setBrandDataFilter({
                                                            ...brandDataFilter,
                                                            name: e.target.value
                                                        })
                                                    }}
                                                    isFastCreate={true}
                                                    dataList={brandList}
                                                    btnRef={brandBtnRef}
                                                    handleSelect={(id) => setDataBody({
                                                        ...dataBody,
                                                        brand_id: id
                                                    })}
                                                    closePopup={() => setIsBrandPopup(false)}
                                                    fetchMoreData={fetchMoreBrand}
                                                />
                                            }
                                        </div>
                                    </div>
                                    <div className="form-item">
                                        <label htmlFor="tags" className="form-label">
                                            Tags
                                            <span
                                                id='tagsCaption'
                                                className="caption-icon"
                                            >
                                                {infoIcon}
                                            </span>
                                            <UncontrolledTooltip
                                                placement="top"
                                                target="tagsCaption"
                                            >
                                                Thêm thẻ cho sản phẩm
                                            </UncontrolledTooltip>
                                        </label>
                                        <div className="form-textfield">
                                            <input
                                                value={dataBody.tags}
                                                type="text"
                                                name="tags"
                                                id="tags"
                                                onChange={e => setDataBody(prevState => {
                                                    return {
                                                        ...prevState,
                                                        tags: e.target.value
                                                    }
                                                })}
                                            />
                                            <fieldset className="input-field"></fieldset>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="box-info-item box-product-status">
                                <div className="info-content">
                                    <div className="form-item">
                                        <label htmlFor="status" className="form-label">
                                            Trạng thái
                                            <span
                                                id='statusCaption'
                                                className="caption-icon"
                                            >
                                                {infoIcon}
                                            </span>
                                            <UncontrolledTooltip
                                                placement="top"
                                                target="statusCaption"
                                            >
                                                Cho phép tìm kiếm sản phẩm và tạo đơn hàng
                                            </UncontrolledTooltip>
                                        </label>
                                        <div className="switch-item">
                                            <p>Cho phép bán</p>
                                            <div className="box-switch">
                                                <div className="btn-switch">
                                                    <input
                                                        type="checkbox"
                                                        name="status"
                                                        className='switch-checkbox'
                                                        id=""
                                                        defaultChecked={true}
                                                        onChange={e => setDataBody(prevState => {
                                                            return {
                                                                ...prevState,
                                                                status: e.target.checked ? "ACTIVE" : "INACTIVE"
                                                            }
                                                        })}
                                                    />
                                                    <span className="switch-bar" ></span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}

export default withAuthorization(ProductUpdate, ["ADMIN","WAREHOUSE_STAFF"]);