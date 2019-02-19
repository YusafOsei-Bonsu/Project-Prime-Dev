const express = require('express')
const router = express.Router()

router.get('/', (req, res, next) => {
    res.status(200).json( {
        message: 'Handling GET requests to /files'
    })
})

router.post('/', (req, res, next) => {
    res.status(201).json( {
        message: 'Handling POST requests to /files'
    })
})

router.get('/:fileId', (req, res, next) => {
    const id = req.params.fileId;
    if(id === 'special') {
        res.status(200).json({
            message: 'Special file',
            id: id
        })
    }
    else {
        res.status(200).json({
            message: 'Generic file'
        })
    }
})

router.patch('/:productId', (req, res, next) => {
    res.status(200).json( {
        message: 'Updated file'
    })
})

router.delete('/:productId', (req, res, next) => {
    res.status(200).json( {
        message: 'Deleted file'
    })
})

module.exports = router
